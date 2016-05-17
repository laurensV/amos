package ai.effect.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.postgresql.util.PGobject;

import ai.effect.server.SqlHandler;

public class Website {
    private int id;
    private String dna_settings;
    private String url;
    private SqlHandler sql;
    /**
     * Constructor
     * Create a website object with parameters and save it to the database.
     * @throws SQLException 
     */
    public Website(String url, String dna_settings, SqlHandler sql) {
        this.url = url;
        this.dna_settings = dna_settings;
        this.sql = sql;
        PreparedStatement stmt = this.sql.prepareStatement("INSERT INTO website (url, dna_settings, generation) VALUES (?, ?, 1) RETURNING id;");

        try {
            PGobject jsonObject = new PGobject();
            jsonObject.setType("json");
            jsonObject.setValue(dna_settings);
            stmt.setString(1, url);
            stmt.setObject(2, jsonObject);
            ResultSet res = stmt.executeQuery();
            
            if ( res.next() ) {
                this.id = res.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
        try {
            stmt.setInt(1, id);
            stmt.executeQuery();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void save(Connection conn) {
        System.out.println("save");
    }

    /**
     * @return the name
     */
    public String getDna() {
        return this.dna_settings;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return this.url;
    }
    
    /**
     * @return the id
     */
    public int getId() {
        return this.id;
    }
}
