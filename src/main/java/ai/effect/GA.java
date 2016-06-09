package ai.effect;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import flanagan.analysis.ANOVA;

import org.json.JSONObject;

import ai.effect.datasource.SqlHandler;

public class GA {
    public static String[] populationFromPopulation() {
        return null;
    }
    public static String[] populationFromDNA(String dna, int size) {
        String[] phenotypes = new String[size];
        for (int i = 0; i < size; i++) {
            phenotypes[i] = phenotypeFromDNA(dna);
        }
        return phenotypes;
    }
    
    public static String mutateIndividual(String phenotype) {
        return null;
    }
    
    public static String phenotypeFromDNA(String dna) {
        JSONObject settings = new JSONObject(dna);
        Iterator<String> keys = settings.keys();
        String phenotype = "{\"items\": [";
        while(keys.hasNext()) {
            // loop to get the dynamic key
            String element_name = (String)keys.next();
            phenotype += "{\"id\": \""+element_name+"\", \"attributes\": ["; 
            // get the value of the dynamic key
            JSONObject element = settings.getJSONObject(element_name);
            Iterator<String> keys2 = element.keys();

            while(keys2.hasNext()) {
                // loop to get the dynamic key
                String attribute_name = (String)keys2.next();

                // get the value of the dynamic key
                JSONObject attribute = element.getJSONObject(attribute_name);
                String value;
                String[] parts;
                if(attribute.getString("type").equals("color")) {
                    int hue, saturation, lightness;
                    parts = attribute.getString("hue").split(":");
                    hue = randInt(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
                    parts = attribute.getString("saturation").split(":");
                    saturation = randInt(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
                    parts = attribute.getString("lightness").split(":");
                    lightness = randInt(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
                    value = "hsl("+hue+", "+saturation+"%, "+lightness+"%)";
                    phenotype += "{\"attribute\": \""+attribute_name+"\", \"value\": \""+value+"\"}, "; 
                }else if(attribute.getString("type").equals("number")) {
                    parts = attribute.getString("numrange").split(":");
                    value = randInt(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])) + "px";
                    phenotype += "{\"attribute\": \""+attribute_name+"\", \"value\": \""+value+"\"}, "; 
                } else if(attribute.getString("type").equals("text")) {
                    parts = attribute.getString("values").split(";");
                    int idx = new Random().nextInt(parts.length);
                    value = (parts[idx]);
                    phenotype += "{\"attribute\": \"text\", \"value\": \""+value+"\"}, "; 
                } else if(attribute.getString("type").equals("custom")) {
                    String[] values = attribute.getString("values").split(";");
                    int idx = new Random().nextInt(values.length);
                    value = (values[idx]);
                    phenotype += "{\"attribute\": \""+attribute_name+"\", \"value\": \""+value+"\"}, ";                 
                }
            }
            phenotype = phenotype.substring(0, phenotype.length() - 2);
            phenotype += "]}, ";
        }
        phenotype = phenotype.substring(0, phenotype.length() - 2);

        phenotype += "]}";        
        return phenotype;
    }
    
    public static void createFitnessFile(int website_id, SqlHandler sql) {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet res = null;
        try {
            con = sql.connectionPool.getConnection();

            stmt = con.prepareStatement("SELECT id FROM individual WHERE website_id = ? ;");
        
            stmt.setInt(1, website_id);
            res = stmt.executeQuery();
            String uuid;
            PrintWriter writer = null;
            try {
                writer = new PrintWriter("fitness-"+website_id+".txt", "UTF-8");
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            /* TODO create file in right format (see doAnova()) */
            writer.println("fitness");
            writer.println("individual_id,score");
            
            while ( res.next() ) {
                uuid = res.getString(1);
                /* TODO: check if previous connection can be reused */
                Connection con2 = null;
                PreparedStatement stmt2 = null;
                ResultSet res2 = null;
                try {
                    con2 = sql.connectionPool.getConnection();
                    stmt2 = con2.prepareStatement("SELECT SUM(score) FROM individual_goal WHERE individual_id = ? GROUP BY session ;");
                    stmt2.setObject(1, uuid, Types.OTHER);
                    res2 = stmt2.executeQuery();
                    int score;
                    boolean scoreAvailable = false;
                    while ( res2.next() ) {
                        scoreAvailable = true;
                        score = res2.getInt(1);
                        writer.println(uuid+","+score);
                    }
                    if(!scoreAvailable) {
                        writer.println(uuid+",");
                    }
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
            }
            writer.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
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
    
    public static void doAnova(String filename) {
        /* 
        
        The text file must be of the following format:

     data title
     number of groups
     group names (one word each), as either a row or a column, e.g.    item1      item2  . . .   itemn
     number of responses per group, as either a row or a column
     response 1 for group 1     response 2 for group 1  . . .   final response for group 1 (all on one line)
     response 1 for group 2     response 2 for group 2  . . .   final response for group 2 (all on one line)
     . . . . 
     response 1 for group n     response 2 for group n  . . .   final response for group n (all on one line)
         */
        ANOVA aov = new ANOVA("fitness");
        aov.readResponseData(filename);
        aov.oneWayAnalysisWithPairComparison("output.txt");
    }
    
    /* TODO: move to another (util/helper) class */
    public static int randInt(int min, int max) {
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
