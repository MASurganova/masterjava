package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.ProjectDao;
import ru.javaops.masterjava.persist.model.Project;

import java.util.List;

public class ProjectTestData {
    public static Project TOP_JAVA;
    public static Project MASTER_JAVA;
    public static Project START_JAVA;
    public static List<Project> PROJECTS;

    public static void init() {
        TOP_JAVA = new Project("TopJava", "top java");
        MASTER_JAVA = new Project("MasterJava", "master java");
        START_JAVA = new Project("StartJava", "start java");
        PROJECTS = ImmutableList.of(MASTER_JAVA, START_JAVA, TOP_JAVA);
    }

    public static void setUp() {
        ProjectDao dao = DBIProvider.getDao(ProjectDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            PROJECTS.forEach(dao::insert);
        });
    }
}
