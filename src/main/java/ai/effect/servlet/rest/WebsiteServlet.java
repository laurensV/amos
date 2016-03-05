package ai.effect.servlet.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;

import ai.effect.models.Website;
import ai.effect.server.SqlHandler;
import ai.effect.servlet.Servlet;

public class WebsiteServlet extends Servlet {
    public WebsiteServlet(SqlHandler sql) {
        super(sql);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String url = req.getParameter("url");
        new Website("test.url", "[]", this.sql);
        try {
            resp.setStatus(HttpStatus.OK_200);
            resp.getWriter().println("hello");
        } catch (Exception e) {}
    }
}
