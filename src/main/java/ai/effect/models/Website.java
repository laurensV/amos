package ai.effect.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.postgresql.util.PGobject;

import ai.effect.datasource.SqlHandler;

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
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet res = null;
        
        try {
            con = this.sql.connectionPool.getConnection();
            stmt = con.prepareStatement("INSERT INTO website (url, dna_settings, generation) VALUES (?, ?, 1) RETURNING id;");


            PGobject jsonObject = new PGobject();
            jsonObject.setType("json");
            jsonObject.setValue(dna_settings);
            stmt.setString(1, url);
            stmt.setObject(2, jsonObject);
            res = stmt.executeQuery();
            
            if ( res.next() ) {
                this.id = res.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (res != null) {
                try {
                    res.close();
                } catch (SQLException e) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                }
            }
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
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = this.sql.connectionPool.getConnection();

            stmt = con.prepareStatement("SELECT * FROM website WHERE id = ? ;");
        
            stmt.setInt(1, id);
            stmt.executeQuery();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                }
            }
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
