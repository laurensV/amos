package ai.effect;

import java.util.HashSet;
import java.util.Set;

import org.glassfish.jersey.server.ResourceConfig;

import ai.effect.util.CorsResponseFilter;
import ai.effect.controller.RestResource;
import ai.effect.controller.DnaResource;
import ai.effect.datasource.DatabaseFactory;

public class AppConfig extends ResourceConfig {
    public AppConfig() {
        // Register components
        registerClasses(RestResource.class, DnaResource.class);


        // Register resources
        register(new DatabaseFactory());
        register(CorsResponseFilter.class);
    }
}
