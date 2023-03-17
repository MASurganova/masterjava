package ru.javaops.masterjava.webapp;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;

public class ThymeleafUtil {

    public ThymeleafUtil() {
    }

    public static TemplateEngine getTemplateEngine(ServletContext context) {
        final ServletContextTemplateResolver resolver = new ServletContextTemplateResolver(context);
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setPrefix("/WEB-INF/templates/");
        resolver.setSuffix(".html");
        resolver.setCacheTTLMs(1000L);
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);
        return templateEngine;
    }
}
