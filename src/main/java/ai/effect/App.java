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
import ai.effect.datasource.SqlHandler;
import ai.effect.servlet.dna.GoalServlet;
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
        Server server = new Server(7070);
        SqlHandler sqlHandler = new SqlHandler();

        ContextHandlerCollection contexts = new ContextHandlerCollection();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/dna");

        FilterHolder cors = context.addFilter(CrossOriginFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        cors.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");
        cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,POST");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");


        context.addServlet(new ServletHolder(new InitServlet(sqlHandler)), "/init/*");
        context.addServlet(new ServletHolder(new GoalServlet(sqlHandler)), "/goal/*");
        context.addServlet(new ServletHolder(new StartServlet(sqlHandler)), "/start/*");
        context.addServlet(new ServletHolder(new StopServlet(sqlHandler)), "/stop/*");


        //ServletContextHandler restContext = new RestService(sqlHandler);

        //1.Creating the resource handler
        ResourceHandler resourceHandler= new ResourceHandler();
        
        //2.Setting Resource Base
        resourceHandler.setResourceBase("js");

        //3.Enabling Directory Listing
        resourceHandler.setDirectoriesListed(true);
        
        //4.Setting Context Source 
        ContextHandler jsContext= new ContextHandler("/js");
    
        //5.Attaching Handlers
        jsContext.setHandler(resourceHandler);

        // Initialize the API servlet
        AppConfig config = new AppConfig();
        ServletContextHandler apiContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
        ServletHolder apiServlet = new ServletHolder(new ServletContainer(config));
        apiContext.addServlet(apiServlet, "/*");


        contexts.setHandlers(new Handler[] { context, apiContext, jsContext });

        server.setHandler(contexts);

        server.start();

        System.out.println("Server started.");
    }

}
