package ai.effect.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Iterator;

import org.json.*;
import org.postgresql.util.PGobject;

import ai.effect.server.SqlHandler;

public class Goal {
    
    private String individual_id;
    public int score;
    public String name;
    public int generation;
    private int id;
    private SqlHandler sql;


    public Goal(String individual_id, String name, int score, SqlHandler sql) {
        this.individual_id = individual_id;
        this.name = name;
        this.score = score;
        this.sql = sql;
        
        PreparedStatement stmt = this.sql.prepareStatement("INSERT INTO individual_goal (individual_id, name, score) VALUES (?, ?, ?) RETURNING id;");

        try {
            stmt.setObject(1, this.individual_id, Types.OTHER);
            stmt.setString(2, this.name);
            stmt.setInt(3, this.score);
            ResultSet res = stmt.executeQuery();
            
            if ( res.next() ) {
                this.id = res.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
