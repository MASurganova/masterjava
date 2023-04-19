package ru.javaops.masterjava.service.mail.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.service.mail.Addressee;

import java.util.List;

public class MailCaseTestData {
    public static List<MailCase> MAILS;

    public static void init() {
        MAILS = ImmutableList.of(MailCase.of(ImmutableList.of(new Addressee("to1 <to1@mail.ru>"),
                new Addressee("to2 <to2@mail.ru>")), ImmutableList.of( new Addressee("cc1 <cc1@mail.ru>"),
                new Addressee("cc2 <cc2@mail.ru>")), "subject1", "body1", "OK"),
                MailCase.of(ImmutableList.of(new Addressee("to1 <to1@mail.ru>"),
                new Addressee("to2 <to2@mail.ru>")), ImmutableList.of( new Addressee("cc1 <cc1@mail.ru>"),
                new Addressee("cc2 <cc2@mail.ru>")), "subject2", "body2", "ERROR"));
    }

    public static void setUp() {
        MailCaseDao dao = DBIProvider.getDao(MailCaseDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            MAILS.forEach(dao::insert);
        });
    }
}
