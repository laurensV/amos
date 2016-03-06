package ai.effect.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import org.json.*;
import org.postgresql.util.PGobject;

import ai.effect.server.SqlHandler;

public class DNA {
    
    public String dna;
    public int profile_id;
    public int website_id;
    private String id;
    private SqlHandler sql;


    public DNA(int profile_id, int website_id, String json, SqlHandler sql) {
        // TODO Auto-generated constructor stub
        JSONObject settings = new JSONObject(json);
        Iterator<String> keys = settings.keys();
        String dna = "{\"items\": [";
        while(keys.hasNext()) {
            // loop to get the dynamic key
            String element_name = (String)keys.next();
            dna += "{\"id\": \""+element_name+"\", \"attributes\": ["; 
            // get the value of the dynamic key
            JSONObject element = settings.getJSONObject(element_name);
            Iterator<String> keys2 = element.keys();

            while(keys2.hasNext()) {
                // loop to get the dynamic key
                String attribute_name = (String)keys2.next();

                // get the value of the dynamic key
                JSONObject attribute = element.getJSONObject(attribute_name);
                if(attribute.getString("type").equals("color")) {
                    /*TODO: get starting values */
                    String value = "hsl(107, 100%, 50%)";
                    dna += "{\"attribute\": \""+element_name+"\", \"value\": \""+value+"\"}, "; 
                }
            }
            dna = dna.substring(0, dna.length() - 2);
            dna += "]}, ";
        }
        dna = dna.substring(0, dna.length() - 2);

        dna += "]}";
        System.out.println(dna);
        //String dna = "{\"items\": [{\"id\": \".btn\", \"attributes\": [{\"attribute\": \"background-color\", \"value\": \"blue\"}]}]}";

        /* TODO: store in database */
        this.dna = dna;
        this.profile_id = profile_id;
        this.website_id = website_id;
        this.sql = sql;
        PreparedStatement stmt = this.sql.prepareStatement("INSERT INTO dna (profile_id, website_id, dna) VALUES (?, ?, ?) RETURNING id;");

        try {
            PGobject jsonObject = new PGobject();
            jsonObject.setType("json");
            jsonObject.setValue(this.dna);
            stmt.setInt(1, profile_id);
            stmt.setInt(2, website_id);
            stmt.setObject(3, jsonObject);
            ResultSet res = stmt.executeQuery();
            
            if ( res.next() ) {
                this.id = res.getString(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
