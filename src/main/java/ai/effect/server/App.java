package ai.effect.server;

import java.net.URI;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import ai.effect.servlet.dna.ClickServlet;
import ai.effect.servlet.dna.InitServlet;
import ai.effect.servlet.dna.StartServlet;
import ai.effect.servlet.dna.StopServlet;


/**
 * Effect.AI main application server
 *
 */
public class App {
    /**
     * Start the main application server
     */
    public static void main(String[] args) throws Exception {
        Server server = new Server(7072);
        SqlHandler sqlHandler = new SqlHandler();

        ContextHandlerCollection contexts = new ContextHandlerCollection();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/dna");

        FilterHolder cors = context.addFilter(CrossOriginFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        cors.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");
        cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");


        context.addServlet(new ServletHolder(new InitServlet(sqlHandler)), "/init/*");
        context.addServlet(new ServletHolder(new ClickServlet(sqlHandler)), "/click/*");
        context.addServlet(new ServletHolder(new StartServlet(sqlHandler)), "/start/*");
        context.addServlet(new ServletHolder(new StopServlet(sqlHandler)), "/stop/*");

        ServletContextHandler restContext = new RestService(sqlHandler);
        server.setHandler(restContext);
        contexts.setHandlers(new Handler[] { context, restContext });

        server.setHandler(contexts);
        server.start();

        System.out.println("Server started.");
    }

}
