package ai.effect.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Iterator;

import org.json.*;
import org.postgresql.util.PGobject;

import ai.effect.datasource.SqlHandler;

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

        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet res = null;
        try {
            con = this.sql.connectionPool.getConnection();
            stmt = con.prepareStatement("SELECT score FROM individual_goal WHERE individual_id = ? AND name = ? AND session = ? ;");

        
            stmt.setObject(1, this.individual_id, Types.OTHER);
            stmt.setString(2, this.name);
            stmt.setString(3, this.session);

            res = stmt.executeQuery();
            
            /* check if there already exist a score for this goal */
            if (res.next()) {
                oldScore = res.getInt(1);
                this.score += oldScore;
                Connection con2 = null;
                PreparedStatement stmt2 = null;
                ResultSet res2 = null;
                

                try {
                    con2 = this.sql.connectionPool.getConnection();

                    stmt2 = con2.prepareStatement("UPDATE individual_goal SET score = ? WHERE individual_id = ? AND name = ? AND session = ? RETURNING id;");

                    stmt2.setInt(1, this.score);
                    stmt2.setObject(2, this.individual_id, Types.OTHER);
                    stmt2.setString(3, this.name);
                    stmt2.setString(4, this.session);

                    res2 = stmt2.executeQuery();
                    
                    if (res2.next()) {
                        this.id = res2.getInt(1);
                    }
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    if (res2 != null) {
                        try {
                            res2.close();
                        } catch (SQLException e) {
                        }
                    }
                    if (stmt2 != null) {
                        try {
                            stmt2.close();
                        } catch (SQLException e) {
                        }
                    }
                    if (con2 != null) {
                        try {
                            con2.close();
                        } catch (SQLException e) {
                        }
                    }
                }
                return;
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
        Connection con3 = null;
        PreparedStatement stmt3 = null;
        ResultSet res3 = null;
        try {
            con3 = this.sql.connectionPool.getConnection();

            /* this is a new goal for this individual, create new row */
            stmt3 = con3.prepareStatement("INSERT INTO individual_goal (individual_id, name, session, score) VALUES (?, ?, ?, ?) RETURNING id;");

        
            stmt3.setObject(1, this.individual_id, Types.OTHER);
            stmt3.setString(2, this.name);
            stmt3.setString(3, this.session);
            stmt3.setInt(4, this.score);
            res3 = stmt3.executeQuery();
            
            if ( res3.next() ) {
                this.id = res3.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (res3 != null) {
                try {
                    res3.close();
                } catch (SQLException e) {
                }
            }
            if (stmt3 != null) {
                try {
                    stmt3.close();
                } catch (SQLException e) {
                }
            }
            if (con3 != null) {
                try {
                    con3.close();
                } catch (SQLException e) {
                }
            }
        }
    }

}
