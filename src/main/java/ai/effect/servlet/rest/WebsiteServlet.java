package ai.effect.servlet.rest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;

import ai.effect.models.DNA;
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
                "\"background-color\": {" +
                  "\"type\": \"color\"," +
                  "\"hue\": \"0;360\"," +
                  "\"saturation\": \"0;100\"," +
                  "\"lightness\": \"0;100\"" +
                "}" +
              "}" +
            "}";
        Website website = new Website(url, json, this.sql);
        int id = website.getId();
        File default_tracker = new File("js/default-tracker.js");
        File tracker = new File("js/tracker-"+id+".js");
        
        /* TODO: foreach profile add starting DNA */
        new DNA(1, id, website.getSettings(), this.sql);

        try {
            Files.copy(default_tracker.toPath(), tracker.toPath(), StandardCopyOption.REPLACE_EXISTING);
            Files.write(tracker.toPath(), Integer.toString(id).getBytes(), StandardOpenOption.APPEND);

            resp.setStatus(HttpStatus.OK_200);
            /* TODO: retrieve server adres and give json back */
            resp.getWriter().println("localhost:7070/js/tracker-"+id+".js");
        } catch (Exception e) {}
    }
}
