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

import org.json.JSONArray;
import org.json.JSONObject;

import ai.effect.datasource.SqlHandler;
import ai.effect.models.Individual;

public class GA {
    public static void newGeneration(int website_id, SqlHandler sql) {
        /* TODO: move increaseGeneration to Website model */
        int generation = increaseGeneration(website_id, sql);
        if(generation != -1){
            Connection con = null;
            PreparedStatement stmt = null;
            ResultSet res = null;
            try {
                con = sql.connectionPool.getConnection();
                stmt = con.prepareStatement("SELECT individual_id, score FROM individual_goal WHERE name = 'conversion' AND session = 'total' ORDER BY score DESC LIMIT 5;");
                res = stmt.executeQuery();
                String individual_id;
                int score;
                int oldPhenotypeIndex = 0;
                String oldPhenotypeArray[] = new String[5];
                String newPhenotypeArray[] = new String[10];

                while ( res.next() ) {
                    individual_id = res.getString(1);
                    score = res.getInt(2);
                    System.out.println(individual_id +":"+score);
                    Connection con2 = null;
                    PreparedStatement stmt2 = null;
                    ResultSet res2 = null;
                    try {
                        con2 = sql.connectionPool.getConnection();
                        stmt2 = con2.prepareStatement("SELECT phenotype FROM individual WHERE id='" + individual_id + "';");
                        res2 = stmt2.executeQuery();
                        if (res2.next()) {
                            oldPhenotypeArray[oldPhenotypeIndex++] = res2.getString(1);
                        }
                    } catch (SQLException e) {
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
                }
                if(oldPhenotypeIndex >= 5){
                    /* create new population by doing crossover */
                    for(int i = 0; i < newPhenotypeArray.length; i++){
                        int randIndex1 = -1;
                        int randIndex2 = -1;
                        while(randIndex1 == randIndex2){
                            randIndex1 = randInt(0, oldPhenotypeIndex - 1);
                            randIndex2 = randInt(0, oldPhenotypeIndex - 1);
                        }
                        newPhenotypeArray[i] = phenotypeFromCrossover(oldPhenotypeArray[randIndex1], oldPhenotypeArray[randIndex2]);
                    }
                    /* TODO: move getDna to Website model */
                    String dna = getDNA(website_id, sql);
                    /* mutate new population */
                    int randIndex;
                    for (int i = 0; i < 3; i++){
                        randIndex = randInt(0, newPhenotypeArray.length - 1);
                        newPhenotypeArray[randIndex] = mutateIndividual(newPhenotypeArray[randIndex], dna);
                    }
                    for (int i = 0; i < newPhenotypeArray.length; i++){
                        new Individual(1, website_id, newPhenotypeArray[i], sql, generation);
                    }
                } else {
                    System.out.println("could not get 5 individuals to create new generation, I only have "+oldPhenotypeIndex);
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
    public static int increaseGeneration(int siteId, SqlHandler sql){
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet res = null;
        int generation = -1;
        try {
            con = sql.connectionPool.getConnection();
            stmt = con.prepareStatement("UPDATE website SET generation = generation + 1 WHERE id = ? RETURNING generation;");
            stmt.setInt(1, siteId);
            res = stmt.executeQuery();
            if ( res.next() ) {
                generation = res.getInt(1);
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
        return generation;
    }
    public static String getDNA(int siteId, SqlHandler sql){
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet res = null;
        String dna = "";
        try {
            con = sql.connectionPool.getConnection();
            stmt = con.prepareStatement("SELECT dna_settings FROM website WHERE id = ? ;");
            stmt.setInt(1, siteId);
            res = stmt.executeQuery();
            if ( res.next() ) {
                dna = res.getString(1);
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
        return dna;
    }
    public static String[] populationFromDNA(String dna, int size) {
        String[] phenotypes = new String[size];
        for (int i = 0; i < size; i++) {
            phenotypes[i] = phenotypeFromDNA(dna);
        }
        return phenotypes;
    }
    
    public static String mutateIndividual(String phenotype, String dna) {
        String newp = phenotypeFromDNA(dna);
        JSONObject p11 = new JSONObject(phenotype);
        JSONArray p22_a = new JSONObject(newp).getJSONArray("items");

        while (phenotype.equals(p11.toString())){
            for (int i = 0; i < p22_a.length(); i++) {
                JSONArray p22_a2 = p22_a.getJSONObject(i).getJSONArray("attributes");
                for (int j= 0; j < p22_a2.length(); j++) {
                    if(Math.random() < 0.25){
                        p11.getJSONArray("items").getJSONObject(i).getJSONArray("attributes").getJSONObject(j).put("value", p22_a2.getJSONObject(j).getString("value"));
                    }
                }
            }
        }
        return p11.toString();
    }
    
    public static String phenotypeFromCrossover(String p1, String p2) {
        JSONObject p11 = new JSONObject(p1);
        JSONArray p22_a = new JSONObject(p2).getJSONArray("items");

        for (int i = 0; i < p22_a.length(); i++) {
            JSONArray p22_a2 = p22_a.getJSONObject(i).getJSONArray("attributes");
            for (int j= 0; j < p22_a2.length(); j++) {
                if(Math.random() < 0.5){
                    p11.getJSONArray("items").getJSONObject(i).getJSONArray("attributes").getJSONObject(j).put("value", p22_a2.getJSONObject(j).getString("value"));
                }
            }
        }
        return p11.toString();
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
        //ANOVA aov = new ANOVA("fitness");
        //aov.readResponseData(filename);
        //aov.oneWayAnalysisWithPairComparison("output.txt");
    }
    
    /* TODO: move to another (util/helper) class */
    public static int randInt(int min, int max) {
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
