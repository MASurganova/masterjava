package ru.javaops.masterjava.upload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.thymeleaf.context.WebContext;
import ru.javaops.masterjava.model.User;
import ru.javaops.masterjava.webapp.ThymeleafListener;

@WebServlet("/")
public class UploadServlet extends HttpServlet {

    private final UserProcessor userProcessor = new UserProcessor();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final WebContext webContext = new WebContext(req, resp, req.getServletContext(), req.getLocale());
        ThymeleafListener.templateEngine.process("upload", webContext, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {final ServletFileUpload upload = new ServletFileUpload();
        final WebContext webContext = new WebContext(req, resp, req.getServletContext(), req.getLocale());

        try {
//            https://commons.apache.org/proper/commons-fileupload/streaming.html

            final FileItemIterator itemIterator = upload.getItemIterator(req);
            while (itemIterator.hasNext()) { //expect that it's only one file
                FileItemStream fileItemStream = itemIterator.next();
                if (!fileItemStream.isFormField()) {
                    try (InputStream is = fileItemStream.openStream()) {
                        List<User> users = userProcessor.process(is);
                        webContext.setVariable("users", users);
                        ThymeleafListener.templateEngine.process("result", webContext, resp.getWriter());
                    }
                    break;
                }
            }
        } catch (Exception e) {
            webContext.setVariable("exception", e);
            ThymeleafListener.templateEngine.process("exception", webContext, resp.getWriter());
        }
    }
}
