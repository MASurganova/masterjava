package ru.javaops.masterjava.service.mail.persist;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.javaops.masterjava.persist.dao.AbstractDaoTest;

import java.util.List;
import static org.junit.Assert.assertEquals;
import static ru.javaops.masterjava.service.mail.persist.MailCaseTestData.MAILS;

public class MailCaseDaoTest extends AbstractDaoTest<MailCaseDao> {

    public MailCaseDaoTest() {
        super(MailCaseDao.class);
    }

    @BeforeClass
    public static void init() throws Exception {
        MailCaseTestData.init();
    }

    @Before
    public void setUp() throws Exception {
        MailCaseTestData.setUp();
    }

    @Test
    public void getAll() throws Exception {
        final List<MailCase> mails = dao.getAll();
        assertEquals(MAILS, mails);
    }
}