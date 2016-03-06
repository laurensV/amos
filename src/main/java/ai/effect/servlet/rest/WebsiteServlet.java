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
        String json = "{" +
                "\"#btn\": {" +
                "\"color\": {" +
                  "\"type\": \"color\"," +
                  "\"hue\": \"0;360\"," +
                  "\"saturation\": \"0;100\"," +
                  "\"lightness\": \"0;100\"" +
                "}" +
              "}" +
            "}";
        Website website = new Website(url, json, this.sql);
        try {
            resp.setStatus(HttpStatus.OK_200);
            resp.getWriter().println(website.getId());
        } catch (Exception e) {}
    }
}
