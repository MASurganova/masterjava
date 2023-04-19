package ru.javaops.masterjava.upload;

import lombok.extern.slf4j.Slf4j;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.GroupDao;
import ru.javaops.masterjava.persist.dao.ProjectDao;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.Project;
import ru.javaops.masterjava.persist.model.type.GroupType;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.stream.XMLStreamException;

@Slf4j
public class ProjectProcessor {
    private final ProjectDao projectDao = DBIProvider.getDao(ProjectDao.class);
    private final GroupDao groupDao = DBIProvider.getDao(GroupDao.class);

    public void process(StaxStreamProcessor processor) throws XMLStreamException {
        while (processor.startElement("Project", "Projects")) {
            Project project = new Project(processor.getAttribute("name"), processor.getElementValue("description"));
            int projectId = projectDao.insertGeneratedId(project);
            String element;
            while (processor.startElement("Group", "Project")) {
                Group newGroup = new Group(processor.getAttribute("name"), GroupType.valueOf(processor.getAttribute("type")), projectId);
                groupDao.insert(newGroup);
            }
        }
    }
}
