package ai.effect.servlet.dna;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.postgresql.util.PGobject;

import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Postal;
import com.maxmind.geoip2.record.Subdivision;

import ai.effect.models.Website;
import ai.effect.datasource.SqlHandler;


public class InitServlet extends DnaServlet {
    public InitServlet(SqlHandler sql) {
        super(sql);
    }

    protected String getResponse(HttpServletRequest req, String[] arguments) {
        HttpSession session = req.getSession(true);
        long unixTime = 0;
        int siteId = Integer.parseInt(arguments[1]);

        if (session == null) {
            System.out.println("no session");
            return "";
        } else {
            String uuid = (String) session.getAttribute("phenotype_uuid");
            PreparedStatement stmt;
            int profileId;
            
            session.setAttribute("start", "");
            session.setAttribute("stop", "");
            
            /* check if this is an existing session*/
            if(uuid != null){
                profileId = (Integer) session.getAttribute("profile_id");
                stmt = this.sql.prepareStatement("SELECT id, phenotype FROM individual WHERE id='"+uuid+"';");
            } else {
                /* new session */
                unixTime = (System.currentTimeMillis() / 1000L);
                session.setAttribute("created", unixTime);
                String visitor_IP = req.getRemoteAddr();
                session.setAttribute("ip", visitor_IP);
                /* TODO: use visitor IP instead of dummy IP*/
                profileId = this.mapToProfile("77.175.185.162", unixTime);
                session.setAttribute("profile_id", profileId);
                /* TODO: hangs sometimes on the prepareStatement */
                stmt = this.sql.prepareStatement("SELECT id, phenotype FROM individual WHERE profile_id="+profileId+" AND website_id="+siteId+" AND generation=((select generation FROM website WHERE id="+siteId+")) ORDER BY RANDOM() LIMIT 1;");
            }
            
            String phenotype = "";
            try {
                ResultSet res = stmt.executeQuery();

                if ( res.next() ) {
                    uuid = res.getString(1);
                    session.setAttribute("phenotype_uuid", uuid);
                    phenotype = res.getString(2);
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("Session = " + session.getId());
            System.out.println("Created = " + session.getAttribute("created"));
            System.out.println("Ip = " + session.getAttribute("ip"));
            System.out.println("Start = " + session.getAttribute("start"));
            System.out.println("Stop = " + session.getAttribute("stop"));
            System.out.println("Profile id = " + session.getAttribute("profile_id"));
            System.out.println("Phenotype uuid = " + session.getAttribute("phenotype_uuid"));

            return phenotype;
            //return "{\"session-id\": \"" + session.getId()
            //        + "\", \"items\": [{\"id\": \".btn\", \"attributes\": [{\"attribute\": \"background-color\", \"value\": \"blue\"}]}]}";
        }
    }
    protected int mapToProfile(String visitor_IP, long visitor_time) {
        Location location = null;
        try {
            location = this.getLocation("77.175.185.162");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (GeoIp2Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        this.insertVisitor(visitor_IP, visitor_time, location);
        
        /* TODO: retrieve profiles instead of dummy profile */
        double profile_lat = 52.132633 , profile_long = 5.291266;
        double profile_time = 43200;
        int profile_id = 1;
        
        double distance = this.distance(location.getLatitude(), location.getLongitude(), profile_lat, profile_long);
        double timeDiff = Math.abs(profile_time - visitor_time % (24*3600));
        System.out.printf("distance %f\n", distance);
        System.out.printf("time difference %f\n", timeDiff);
        /* TODO: select best matching profile */
        
        return profile_id;
    }
    
    /* get the distance in kilometres with the Haversine formula */
    protected double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist * 1.609344);
    }
    protected double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    protected double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
    
    protected int insertVisitor(String visitor_IP, long time, Location location){
        // TODO: insert in table visitors
        return 1;
    }
    
    protected Location getLocation(String IP) throws IOException, GeoIp2Exception {
        // A File object pointing to your GeoIP2 or GeoLite2 database
        File database = new File("src/main/resources/db/GeoLite2-City.mmdb");

        // This creates the DatabaseReader object, which should be reused across
        // lookups.
        // with cache: (Using this cache, lookup performance is significantly improved at the cost of a small (~2MB) memory overhead.)
        DatabaseReader reader = new DatabaseReader.Builder(database).withCache(new CHMCache()).build();
        // without cache:
        //DatabaseReader reader = new DatabaseReader.Builder(database).build();

        InetAddress ipAddress = InetAddress.getByName(IP);

        // Replace "city" with the appropriate method for your database, e.g.,
        // "country".
        CityResponse response = reader.city(ipAddress);
//        Country country = response.getCountry();
//        System.out.println(country.getIsoCode());            // 'US'
//        System.out.println(country.getName());               // 'United States'
//        System.out.println(country.getNames().get("zh-CN")); // '美国'
//
//        Subdivision subdivision = response.getMostSpecificSubdivision();
//        System.out.println(subdivision.getName());    // 'Minnesota'
//        System.out.println(subdivision.getIsoCode()); // 'MN'
//
//        City city = response.getCity();
//        System.out.println(city.getName()); // 'Minneapolis'
//
//        Postal postal = response.getPostal();
//        System.out.println(postal.getCode()); // '55455'

        return response.getLocation();
    }
}
