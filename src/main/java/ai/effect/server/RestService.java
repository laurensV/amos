package ai.effect.server;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import ai.effect.servlet.dna.InitServlet;
import ai.effect.servlet.rest.WebsiteServlet;

public class RestService extends ServletContextHandler {
    /**
     *
     */
    public RestService() {
        super(ServletContextHandler.SESSIONS);
        this.setContextPath("/api");
        this.addServlet(new ServletHolder(new WebsiteServlet()), "/website/add/*");
    }
}
