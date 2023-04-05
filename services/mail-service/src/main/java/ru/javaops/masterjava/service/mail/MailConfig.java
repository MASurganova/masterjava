package ru.javaops.masterjava.service.mail;

import com.typesafe.config.Config;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import ru.javaops.masterjava.config.Configs;

import javax.mail.Authenticator;
import java.nio.charset.StandardCharsets;

public class MailConfig {

    final private static MailConfig INSTANCE = new MailConfig(Configs.getConfig("mail.conf", "mail"));
    final private String host;
    final private int port;
    final private String username;
    final private Authenticator auth;
    final private boolean useSSL;
    final private boolean useTLS;
    final private boolean debug;
    final private String fromName;

    public MailConfig (Config config) {
        host = config.getString("host");
        port = config.getInt("port");
        useSSL = config.getBoolean("useSSL");
        useTLS = config.getBoolean("useTSL");
        debug = config.getBoolean("debug");
        username = config.getString("username");
        auth = new DefaultAuthenticator(username, config.getString("password"));
        fromName = config.getString("fromName");
    }

    private <T extends Email> T prepareEmail (T email) throws EmailException {
        email.setHostName(host);
        email.setFrom(username, fromName);
        if (useSSL)  {
            email.setSslSmtpPort(String.valueOf(port));
        } else email.setSmtpPort(port);
        email.setSSLOnConnect(useSSL);
        email.setStartTLSEnabled(useTLS);
        email.setAuthenticator(auth);
        email.setCharset(StandardCharsets.UTF_8.name());
        return email;
    }

    public static HtmlEmail createHtmlEmail()  throws EmailException {
        HtmlEmail email = new HtmlEmail();
        return INSTANCE.prepareEmail(email);
    }
}
