package ai.effect.server;

import java.net.URI;
import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import ai.effect.server.RestService;

import ai.effect.servlet.dna.ClickServlet;
import ai.effect.servlet.dna.InitServlet;
import ai.effect.servlet.dna.StartServlet;
import ai.effect.servlet.dna.StopServlet;

import org.apache.commons.dbcp.*;

/**
 * Effect.AI main application server
 *
 */
public class App {
    /**
     * Pool of database connections
     */
    private BasicDataSource connectionPool;

    /**
     * Start the main application server
     */
    public static void main(String[] args) throws Exception {
        App app = new App();

        Server server = new Server(7070);

        ContextHandlerCollection contexts = new ContextHandlerCollection();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/dna");

        FilterHolder cors = context.addFilter(CrossOriginFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        cors.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");
        cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");

        context.addServlet(new ServletHolder(new InitServlet()), "/init/*");
        context.addServlet(new ServletHolder(new ClickServlet()), "/click/*");
        context.addServlet(new ServletHolder(new StartServlet()), "/start/*");
        context.addServlet(new ServletHolder(new StopServlet()), "/stop/*");

        ServletContextHandler restContext = new RestService();
        server.setHandler(restContext);
        contexts.setHandlers(new Handler[] { context, restContext });

        server.setHandler(contexts);
        server.start();

        System.out.println("Server started.");
    }

    public App() {
        System.out.println("con3");
        this.setupDatabase();
    }

    public void setupDatabase() {
        System.out.println("db url");
        System.out.println(System.getenv("db.url"));
        try {
            // DATABASE_URL=postgres://postgres:@localhost/effect
            URI dbUri = new URI(System.getenv().get("DATABASE_URL"));
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath();
            System.out.println("test");
            System.out.println(dbUrl);
        } catch (Exception e) {

        }
    }
}
