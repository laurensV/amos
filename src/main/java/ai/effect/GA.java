package ai.effect;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.json.JSONObject;

public class GA {
    public static String[] PopulationFromPopulation() {
        return null;
    }
    public static String[] PopulationFromDNA(String dna, int size) {
        String[] phenotypes = new String[size];
        for (int i = 0; i < size; i++) {
            phenotypes[i] = phenotypeFromDNA(dna);
        }
        return phenotypes;
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
    /* TODO: move to another (util/helper) class */
    private static int randInt(int min, int max) {
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
