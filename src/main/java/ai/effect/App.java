package ai.effect;

import java.net.URI;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.servlet.ServletContainer;

import ai.effect.AppConfig;

/**
 * Effect.AI main application server
 *
 */
public class App {
    /**
     * Start the main application server
     */
    public static void main(String[] args) throws Exception {
        Server server = new Server(7070);

        ContextHandlerCollection contexts = new ContextHandlerCollection();

        // Create a resource servlet
        ResourceHandler resourceHandler= new ResourceHandler();
        resourceHandler.setResourceBase("js");
        resourceHandler.setDirectoriesListed(true);
        ContextHandler jsContext= new ContextHandler("/js");
        jsContext.setHandler(resourceHandler);

        // Create the application servlet
        AppConfig config = new AppConfig();
        ServletContextHandler appContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
        ServletHolder appServlet = new ServletHolder(new ServletContainer(config));
        appContext.addServlet(appServlet, "/*");
        FilterHolder cors = appContext.addFilter(CrossOriginFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        cors.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");
        cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,POST");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");

        contexts.setHandlers(new Handler[] { appContext, jsContext });

        server.setHandler(contexts);

        server.start();

        System.out.println("Server started.");
    }

}
