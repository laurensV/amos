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
    public String session;
    public int generation;
    private int id;
    private SqlHandler sql;


    public Goal(String individual_id, String name, String session, int score, SqlHandler sql) {
        int oldScore;
        this.individual_id = individual_id;
        this.name = name;
        this.score = score;
        this.session = session;
        this.sql = sql;

        
        PreparedStatement stmt = this.sql.prepareStatement("SELECT score FROM individual_goal WHERE individual_id = ? AND name = ? AND session = ? ;");

        try {
            stmt.setObject(1, this.individual_id, Types.OTHER);
            stmt.setString(2, this.name);
            stmt.setString(3, this.session);

            ResultSet res = stmt.executeQuery();
            
            /* check if there already exist a score for this goal */
            if (res.next()) {
                oldScore = res.getInt(1);
                this.score += oldScore;
                PreparedStatement stmt2 = this.sql.prepareStatement("UPDATE individual_goal SET score = ? WHERE individual_id = ? AND name = ? AND session = ? RETURNING id;");

                try {
                    stmt2.setInt(1, this.score);
                    stmt2.setObject(2, this.individual_id, Types.OTHER);
                    stmt2.setString(3, this.name);
                    stmt2.setString(4, this.session);

                    ResultSet res2 = stmt2.executeQuery();
                    
                    if (res2.next()) {
                        this.id = res2.getInt(1);
                    }
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        /* this is a new goal for this individual, create new row */
        PreparedStatement stmt3 = this.sql.prepareStatement("INSERT INTO individual_goal (individual_id, name, session, score) VALUES (?, ?, ?, ?) RETURNING id;");

        try {
            stmt3.setObject(1, this.individual_id, Types.OTHER);
            stmt3.setString(2, this.name);
            stmt3.setString(3, this.session);
            stmt3.setInt(4, this.score);
            ResultSet res3 = stmt3.executeQuery();
            
            if ( res3.next() ) {
                this.id = res3.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
