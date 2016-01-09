package ai.effect.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import ai.effect.servlet.ExampleServlet;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	Server server = new Server(7070);
    	ServletContextHandler handler = new ServletContextHandler(server, "/example");
    	handler.addServlet(ExampleServlet.class, "/");
    	try {
			server.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println( "Hello World!" );
    }
}
