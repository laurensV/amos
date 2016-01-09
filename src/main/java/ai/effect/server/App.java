package ai.effect.server;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.SessionManager;
import org.eclipse.jetty.server.session.HashSessionIdManager;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import ai.effect.servlet.ClickServlet;
import ai.effect.servlet.InitServlet;
import ai.effect.servlet.StartServlet;
import ai.effect.servlet.StopServlet;

/**
 * Effect.AI main application server
 *
 */
public class App {
    public static void main(String[] args) throws Exception {
        Server server = new Server(7070);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/dna");
        server.setHandler(context);

        HashSessionIdManager hashSessionIdManager = new HashSessionIdManager();
        SessionHandler sessionHandler = new SessionHandler();
        SessionManager sessionManager = new HashSessionManager();
        sessionManager.setSessionIdManager(hashSessionIdManager);
        sessionHandler.setSessionManager(sessionManager);
        sessionHandler.setHandler(context);
        sessionHandler.setServer(server);
        server.setSessionIdManager(hashSessionIdManager);

        FilterHolder cors = context.addFilter(CrossOriginFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        cors.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");
        cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");

        context.addServlet(new ServletHolder(new InitServlet()), "/init");
        context.addServlet(new ServletHolder(new ClickServlet()), "/click");
        context.addServlet(new ServletHolder(new StartServlet()), "/start");
        context.addServlet(new ServletHolder(new StopServlet()), "/stop");

        server.start();

        System.out.println("Server started.");
    }
}
