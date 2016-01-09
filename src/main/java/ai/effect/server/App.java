package ai.effect.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import ai.effect.servlet.ClickServlet;
import ai.effect.servlet.ExampleServlet;
import ai.effect.servlet.InitServlet;
import ai.effect.servlet.StartServlet;
import ai.effect.servlet.StopServlet;

/**
 * Effect.AI main application server
 *
 */
public class App {
    public static void main( String[] args ) throws Exception {
    	Server server = new Server(7070);
    	ServletContextHandler context = new ServletContextHandler();
    	context.setContextPath("/dna");
    	server.setHandler(context);
    	
    	context.addServlet(new ServletHolder(new InitServlet()), "/init");
    	context.addServlet(new ServletHolder(new ClickServlet()), "/click");
    	context.addServlet(new ServletHolder(new StartServlet()), "/start");
    	context.addServlet(new ServletHolder(new StopServlet()), "/stop");
    	
		server.start();

        System.out.println( "Server started." );
    }
}
