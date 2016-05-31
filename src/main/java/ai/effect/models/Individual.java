package ai.effect.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import org.json.*;
import org.postgresql.util.PGobject;

import ai.effect.datasource.SqlHandler;

public class Individual {
    
    public String phenotype;
    public int profile_id;
    public int website_id;
    public int generation;
    private String id;
    private SqlHandler sql;

    public Individual(int profile_id, int website_id, String phenotype, SqlHandler sql, int generation) {
        this.phenotype = phenotype;
        this.profile_id = profile_id;
        this.website_id = website_id;
        this.generation = generation;
        this.sql = sql;
        
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet res = null;
        try {
            con = this.sql.connectionPool.getConnection();
            stmt = con.prepareStatement("INSERT INTO individual (profile_id, website_id, phenotype, generation) VALUES (?, ?, ?, ?) RETURNING id;");
            PGobject jsonObject = new PGobject();
            jsonObject.setType("json");
            jsonObject.setValue(this.phenotype);
            stmt.setInt(1, this.profile_id);
            stmt.setInt(2, this.website_id);
            stmt.setObject(3, jsonObject);
            stmt.setInt(4, this.generation);
            res = stmt.executeQuery();
            
            if ( res.next() ) {
                this.id = res.getString(1);
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
}
