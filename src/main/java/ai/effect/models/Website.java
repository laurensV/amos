package ai.effect.models;

import java.sql.Connection;

public class Website {
    private int id;
    private String name;
    private String url;

    /**
     * Constructor
     * Create a website object with parameters and save it to the database.
     */
    public Website(String name, String url, Connection conn) {
        this.name = name;
        this.url = url;
        this.save(conn);
    }

    /**
     * Constructor
     * Create a website object from a database connection.
     *
     * @param conn a jdbc database connection
     */
    public Website(int id, Connection conn) {
        
    }

    public void save(Connection conn) {
        System.out.println("save");
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }
}
