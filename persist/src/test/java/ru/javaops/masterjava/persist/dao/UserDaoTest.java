package ru.javaops.masterjava.persist.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.javaops.masterjava.persist.UserTestData;
import ru.javaops.masterjava.persist.model.User;
import ru.javaops.masterjava.persist.model.UserFlag;

import java.util.List;

import static ru.javaops.masterjava.persist.UserTestData.FIST5_USERS;
import static ru.javaops.masterjava.persist.UserTestData.USER1;

public class UserDaoTest extends AbstractDaoTest<UserDao> {

    public UserDaoTest() {
        super(UserDao.class);
    }

    @BeforeClass
    public static void init() throws Exception {
        UserTestData.init();
    }

    @Before
    public void setUp() throws Exception {
        UserTestData.setUp();
    }

    @Test
    public void getWithLimit() {
        List<User> users = dao.getWithLimit(5);
        Assert.assertEquals(FIST5_USERS, users);
    }

    @Test
    public void insertAll() {
        dao.clean();
        dao.insertAll(FIST5_USERS, 100);
        Assert.assertEquals(FIST5_USERS.size(), dao.getWithLimit(100).size());
    }

    @Test
    public void duplicateEmail() {
        int size = dao.getWithLimit(Integer.MAX_VALUE).size();
        User duplicate = new User("duplicate", USER1.getEmail(), UserFlag.active);
        dao.insert(duplicate);
        Assert.assertEquals(size, dao.getWithLimit(Integer.MAX_VALUE).size());
    }
}