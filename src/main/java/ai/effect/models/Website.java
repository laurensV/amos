package ai.effect.models;

import java.sql.Connection;
import java.sql.PreparedStatement;

import ai.effect.server.SqlHandler;

public class Website {
    private int id;
    private String dna_settings;
    private String url;
    private SqlHandler sql;
    /**
     * Constructor
     * Create a website object with parameters and save it to the database.
     */
    public Website(String url, String dna_settings, SqlHandler sql) {
        this.url = url;
        this.dna_settings = dna_settings;
        this.sql = sql;
        PreparedStatement stmt = this.sql.prepareStatement("INSERT INTO website (url, dna_settings) VALUES (?, ?);");
        stmt.setString(1, url);
        stmt.setString(2, dna_settings);
        stmt.executeUpdate();
    }

    /**
     * Constructor
     * Create a website object from a database connection.
     *
     * @param conn a jdbc database connection
     */
    public Website(int id, SqlHandler sql) {
        this.sql = sql;
        PreparedStatement stmt = this.sql.prepareStatement("SELECT * FROM website WHERE id = ? ;");
        stmt.setInt(1, id);
        stmt.executeQuery();
    }

    public void save(Connection conn) {
        System.out.println("save");
    }

    /**
     * @return the name
     */
    public String getSettings() {
        return this.dna_settings;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return this.url;
    }
}
