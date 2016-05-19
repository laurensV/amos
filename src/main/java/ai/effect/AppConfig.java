package ai.effect;

import java.util.HashSet;
import java.util.Set;

import org.glassfish.jersey.server.ResourceConfig;

import ai.effect.controller.RestResource;
import ai.effect.datasource.DatabaseFactory;

public class AppConfig extends ResourceConfig {
    public AppConfig() {
        // Register components
        registerClasses(RestResource.class);

        // Register resources
        register(new DatabaseFactory());
    }
}
