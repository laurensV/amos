package ai.effect.servlet.rest;

import java.io.IOException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;

public class WebsiteServlet extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        String JDBC = "jdbc:postgresql://localhost/effect";
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection(JDBC, "postgres", "");
            stmt = conn.createStatement();
            String sql = "INSERT INTO website (url, dna_settings) VALUES ('123', '[]');";
            ResultSet rs = stmt.executeQuery(sql);
        } catch (Exception e) { e.printStackTrace(); }
        resp.setStatus(HttpStatus.OK_200);
        resp.getWriter().println("hello");
    }
}
