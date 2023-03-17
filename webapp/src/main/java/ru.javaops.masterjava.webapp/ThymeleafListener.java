package ru.javaops.masterjava.webapp;

import org.thymeleaf.TemplateEngine;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

@WebListener
public class ThymeleafListener {

    public static TemplateEngine templateEngine;

    public static void contextInitialized(ServletContextEvent servletContextEvent) {
        templateEngine = ThymeleafUtil.getTemplateEngine(servletContextEvent.getServletContext());
    }

}
