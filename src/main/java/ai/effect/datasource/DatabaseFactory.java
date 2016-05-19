package ai.effect.datasource;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class DatabaseFactory extends AbstractBinder implements Factory<SqlHandler> {

    // For now use a single SqlHandler
    private final SqlHandler sql;

    @Inject
    public DatabaseFactory() {
        this.sql = new SqlHandler();
    }

    @Override
    public void configure() {
        bindFactory(this).to(SqlHandler.class);
    }

    @Override
    public SqlHandler provide() {
        return sql;
    }

    @Override
    public void dispose(SqlHandler t) {
    }
}
