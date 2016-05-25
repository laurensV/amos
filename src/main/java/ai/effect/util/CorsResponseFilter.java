package ai.effect.util;

import java.io.IOException;
import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;

import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;

public class CorsResponseFilter
    implements ContainerResponseFilter {

    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
        throws IOException {
        /* DOESN'T WORK
        MultivaluedMap<String, Object> headers = responseContext.getHeaders();

        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET, POST");			
        headers.add("Access-Control-Allow-Headers", "X-Requested-With,Content-Type,Accept,Origin");
        headers.add("Access-Control-Allow-Credentials", "true");*/
        
    }

}
