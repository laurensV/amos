package ai.effect.datasource;

import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.commons.dbcp.*;

public class SqlHandler {
    /**
     * Pool of database connections
     */
    public BasicDataSource connectionPool;

    /**
     * Constructor
     */
    public SqlHandler() {
        this.setupDatabase();
        System.out.println("Database connected!");
    }

    /**
     *
     */
    public void setupDatabase() {
        connectionPool = new BasicDataSource();

        try {
            // DATABASE_URL=postgres://postgres:@localhost/effect
            // URI dbUri = new URI(System.getenv().get("DATABASE_URL"));
            URI dbUri = new URI("postgres://postgres:postgres@localhost:5432/effect");
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

    /*
    public PreparedStatement prepareStatement(String sql) {
        PreparedStatement stmt = null;
        try {
            Connection connection = connectionPool.getConnection();
            stmt = connection.prepareStatement(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stmt;
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

    public void executeUpdate(String sql) {
        try {
            Connection connection = connectionPool.getConnection();
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */

}
