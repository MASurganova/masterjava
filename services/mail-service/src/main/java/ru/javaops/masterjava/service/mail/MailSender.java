package ru.javaops.masterjava.service.mail;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.service.mail.persist.MailCase;
import ru.javaops.masterjava.service.mail.persist.MailCaseDao;

import java.util.List;

@Slf4j
public class MailSender {

    private static MailCaseDao mailCaseDao = DBIProvider.getDao(MailCaseDao.class);
    static void sendMail(List<Addressee> to, List<Addressee> cc, String subject, String body) {
        log.info("Send email to: " + to + ", cc: " +  cc + ", subject: " + subject);
        String state = "OK";
        try {
            HtmlEmail email = MailConfig.createHtmlEmail();
            for(Addressee addressee : to) {
                email.addTo(addressee.getEmail(), addressee.getName());
            }
            for(Addressee addressee : cc) {
                email.addCc(addressee.getEmail(), addressee.getName());
            }
            email.setSubject(subject);
            email.setHtmlMsg(body);
            email.setHeaders(ImmutableMap.of("List-Unsubscribe", "<mailto:m150803@mail.ru?subject=Unsubscribe&body=Unsubscribe>"));
            email.send();
        } catch (EmailException e) {
            log.error(e.getMessage(), e);
            state = e.getMessage();
        }
        mailCaseDao.insert(MailCase.of(to, cc, subject, body, state));
        log.info("Send with state: " + state);
    }
}
