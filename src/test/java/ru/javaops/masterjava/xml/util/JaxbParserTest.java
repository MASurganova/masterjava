package ru.javaops.masterjava.xml.util;

import com.google.common.io.Resources;
import org.junit.Test;
import ru.javaops.masterjava.xml.schema.*;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class JaxbParserTest {
    private static final JaxbParser JAXB_PARSER = new JaxbParser(ObjectFactory.class);

    static {
        JAXB_PARSER.setSchema(Schemas.ofClasspath("payload.xsd"));
    }

    @Test
    public void testPayload() throws Exception {
//        JaxbParserTest.class.getResourceAsStream("/city.xml")
        Payload payload = JAXB_PARSER.unmarshal(
                Resources.getResource("payload.xml").openStream());
        String strPayload = JAXB_PARSER.marshal(payload);
        JAXB_PARSER.validate(strPayload);
        System.out.println(strPayload);
    }

    @Test
    public void testCity() throws Exception {
        JAXBElement<CityType> cityElement = JAXB_PARSER.unmarshal(
                Resources.getResource("city.xml").openStream());
        CityType city = cityElement.getValue();
        JAXBElement<CityType> cityElement2 =
                new JAXBElement<>(new QName("http://javaops.ru", "City"), CityType.class, city);
        String strCity = JAXB_PARSER.marshal(cityElement2);
        JAXB_PARSER.validate(strCity);
        System.out.println(strCity);
    }

    @Test
    public void userForProject() throws Exception {
        Payload payload = JAXB_PARSER.unmarshal(
                Resources.getResource("payload.xml").openStream());
        List<Project> project = payload.getProjects().getProject();
        List<User> users = payload.getUsers().getUser();
        Set<Project.Group> projectsGroups =  new HashSet<>();
        project.stream().filter(p -> p.getName().equalsIgnoreCase("topjava"))
                .map(Project::getGroup).forEach(projectsGroups::addAll);
        users.stream().filter(user -> !Collections.disjoint(projectsGroups, user.getGroupRefs()))
                .sorted(Comparator.comparing(User::getFullName).thenComparing(User::getEmail))
                .forEach(user -> System.out.println(user.getFullName() + " " + user.getEmail()));

    }
}