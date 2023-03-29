package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.dao.GroupDao;
import ru.javaops.masterjava.persist.model.Group;

import java.util.List;

import static ru.javaops.masterjava.persist.ProjectTestData.*;
import static ru.javaops.masterjava.persist.model.GroupFlag.*;

public class GroupTestData {
    public static Group TOP_JAVA1;
    public static Group TOP_JAVA2;
    public static Group TOP_JAVA3;
    public static Group MASTER_JAVA1;
    public static Group MASTER_JAVA2;
    public static Group START_JAVA0;
    public static List<Group> GROUPS;

    public static void init() {
        TOP_JAVA1 = new Group("topjava1", TOP_JAVA.getId(), FINISHED);
        TOP_JAVA2 = new Group("topjava2", TOP_JAVA.getId(), FINISHED);
        TOP_JAVA3 = new Group("topjava3", TOP_JAVA.getId(), CURRENT);
        MASTER_JAVA1 = new Group("masterjava1", MASTER_JAVA.getId(), FINISHED);
        MASTER_JAVA2 = new Group("masterjava2", MASTER_JAVA.getId(), CURRENT);
        START_JAVA0 = new Group("startjava0", START_JAVA.getId(), CREATE);
        GROUPS = ImmutableList.of(MASTER_JAVA1, MASTER_JAVA2, START_JAVA0, TOP_JAVA1, TOP_JAVA2, TOP_JAVA3);
    }

    public static void setUp() {
        GroupDao dao = DBIProvider.getDao(GroupDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            GROUPS.forEach(dao::insert);
        });
    }
}
