package ru.javaops.masterjava.xml.util;

import com.google.common.io.Resources;
import joptsimple.internal.Strings;
import org.junit.Test;
import ru.javaops.masterjava.xml.schema.User;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.util.*;

public class StaxStreamProcessorTest {
    @Test
    public void readCities() throws Exception {
        try (StaxStreamProcessor processor =
                     new StaxStreamProcessor(Resources.getResource("payload.xml").openStream())) {
            XMLStreamReader reader = processor.getReader();
            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLEvent.START_ELEMENT) {
                    if ("City".equals(reader.getLocalName())) {
                        System.out.println(reader.getElementText());
                    }
                }
            }
        }
    }

    @Test
    public void readCities2() throws Exception {
        try (StaxStreamProcessor processor =
                     new StaxStreamProcessor(Resources.getResource("payload.xml").openStream())) {
            String city;
            while ((city = processor.getElementValue("City")) != null) {
                System.out.println(city);
            }
        }
    }

    @Test
    public void usersForProject() throws Exception {
        try (StaxStreamProcessor processor =
                     new StaxStreamProcessor(Resources.getResource("payload.xml").openStream())) {
            List<String> groups = new ArrayList<>();
            Set<User> users = new TreeSet<>(Comparator.comparing(User::getFullName).thenComparing(User::getEmail));
            projectLoop:
            while (processor.doUntil(StartElement.START_ELEMENT, "Project")) {
                if (processor.getAttribute("name").equals("masterjava")) {
                    String element;
                    while ((element = processor.doUntilAny(StartElement.START_ELEMENT, "Project", "Users", "Group")) != null) {
                        if (!element.equals("Group")) break projectLoop;
                        groups.add(processor.getAttribute("name"));
                    }
                }
            }
            while (processor.doUntil(StartElement.START_ELEMENT, "User")) {
                List<String> userGroups = Arrays.asList(processor.getAttribute("groupRefs").split(" "));
                if (!Collections.disjoint(groups, userGroups)) {
                    User user = new User();
                    user.setEmail(processor.getAttribute("email"));
                    user.setFullName(processor.getElementValue("fullName"));
                    users.add(user);
                }
            }
            users.forEach(u -> System.out.println(u.getFullName() + " " + u.getEmail()));
        }
    }


}