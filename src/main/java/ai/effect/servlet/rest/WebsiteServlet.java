package ai.effect.servlet.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;

import ai.effect.server.SqlHandler;
import ai.effect.servlet.Servlet;

public class WebsiteServlet extends Servlet {
    public WebsiteServlet(SqlHandler sql) {
        super(sql);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        this.sql.executeUpdate("INSERT INTO website (url, dna_settings) VALUES ('2434', '[]');");
        try {
            resp.setStatus(HttpStatus.OK_200);
            resp.getWriter().println("hello");
        } catch (Exception e) {}
    }
}
