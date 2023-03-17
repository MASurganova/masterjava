package ru.javaops.masterjava.upload;

import ru.javaops.masterjava.model.User;
import ru.javaops.masterjava.model.UserFlag;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UserProcessor {

    public static List<User> process(final InputStream is) throws XMLStreamException {
        final StaxStreamProcessor processor = new StaxStreamProcessor(is);
        List<User> users = new ArrayList<>();
        while (processor.doUntil(XMLEvent.START_ELEMENT, "User")) {
            String fullName = processor.getReader().getElementText();
            String email = processor.getAttribute("email");
            UserFlag flag = UserFlag.valueOf(processor.getAttribute("flag"));
            users.add(new User(fullName, email, flag));
        }
        return users;
    }
}
