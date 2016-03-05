package ai.effect.server;

import java.net.URI;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.commons.dbcp.*;

public class SqlHandler {
    /**
     * Pool of database connections
     */
    private BasicDataSource connectionPool;

    /**
     * Constructor
     */
    public SqlHandler() {
        this.setupDatabase();
    }

    /**
     *
     */
    public void setupDatabase() {
        connectionPool = new BasicDataSource();

        try {
            // DATABASE_URL=postgres://postgres:@localhost/effect
            URI dbUri = new URI(System.getenv().get("DATABASE_URL"));
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath();

            if (dbUri.getUserInfo() != null) {
                String[] userInfo = dbUri.getUserInfo().split(":");
                connectionPool.setUsername(userInfo[0]);
                if (userInfo.length > 1)
                    connectionPool.setPassword(userInfo[1]);
            }
            connectionPool.setDriverClassName("org.postgresql.Driver");
            connectionPool.setUrl(dbUrl);
            connectionPool.setInitialSize(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet executeSql(String sql) {
        ResultSet rs = null;
        try {
            Connection connection = connectionPool.getConnection();
            Statement stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rs;
    }
}