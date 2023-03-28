package ru.javaops.masterjava.upload;

import one.util.streamex.StreamEx;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.UserDao;
import ru.javaops.masterjava.persist.model.User;
import ru.javaops.masterjava.persist.model.UserFlag;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.JaxbUnmarshaller;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UserProcessor {
    private static final JaxbParser jaxbParser = new JaxbParser(ObjectFactory.class);
    private static final int TREAD_NUMBER = 4;
    private ExecutorService executorService = Executors.newFixedThreadPool(TREAD_NUMBER);
    private UserDao userDao = DBIProvider.getDao(UserDao.class);

    public static class FailedEmail {
        public String emailOrRange;
        public String reason;

        public FailedEmail(String emailOrRange, String reason) {
            this.emailOrRange = emailOrRange;
            this.reason = reason;
        }

        @Override
        public String toString() {
            return emailOrRange + " : " + reason;
        }
    }

    public List<FailedEmail> process(final InputStream is, int chunkSize) throws XMLStreamException, JAXBException {
        final StaxStreamProcessor processor = new StaxStreamProcessor(is);

        return new Callable<List<FailedEmail>>() {
            class ChunkFuture {
                Future<List<String>> future;
                String emailRange;
                public ChunkFuture(List<User> chunk, Future<List<String>> future) {
                    this.future = future;
                    this.emailRange = chunk.get(0).getEmail();
                    if(chunk.size() > 1) {
                        emailRange += '-' + chunk.get(chunk.size()-1).getEmail();
                    }
                }
            }
            public List<FailedEmail> call() throws XMLStreamException, JAXBException {
                List<ChunkFuture> futures = new ArrayList<>();
                List<User> chunk = new ArrayList<>(chunkSize);
                int id = userDao.getSeqAndSkip(chunkSize);
                JaxbUnmarshaller unmarshaller = jaxbParser.createUnmarshaller();
                while (processor.doUntil(XMLEvent.START_ELEMENT, "User")) {
                    ru.javaops.masterjava.xml.schema.User xmlUser = unmarshaller.unmarshal(processor.getReader(), ru.javaops.masterjava.xml.schema.User.class);
                    final User user = new User(id++, xmlUser.getValue(), xmlUser.getEmail(), UserFlag.valueOf(xmlUser.getFlag().value()));
                    chunk.add(user);
                    if (chunk.size() == chunkSize) {
                        futures.add(submit(chunkSize, chunk));
                        chunk = new ArrayList<>(chunkSize);
                        id = userDao.getSeqAndSkip(chunkSize);
                    }
                }
                if (!chunk.isEmpty()) {
                    futures.add(submit(chunkSize, chunk));
                }
                List<FailedEmail> failed = new ArrayList<>();
                futures.forEach(f -> {
                    try {
                        failed.addAll(StreamEx.of(f.future.get()).map(email -> new FailedEmail(email, "already present")).toList());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                return failed;
            }

            private ChunkFuture submit(int chunkSize, List<User> chunk) {
                return new ChunkFuture(chunk, executorService.submit(() -> userDao.insertAndGetConflictEmail(chunk, chunkSize)));
            }
        }.call();
    }
}
