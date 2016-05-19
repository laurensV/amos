package ai.effect.servlet.dna;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ai.effect.datasource.SqlHandler;

import ai.effect.models.Goal;
import ai.effect.models.Individual;

public class GoalServlet extends DnaServlet {
    public GoalServlet(SqlHandler sql) {
        super(sql);
    }

    protected String getResponse(HttpServletRequest req, String[] arguments) {
        HttpSession session = req.getSession(false);
        if (session == null) {
            System.out.println("no session");
            return "{\"code\": \"ERROR\"}";
        } else {
            String name = arguments[1];
            int score = Integer.parseInt(arguments[2]);
            String session_id = session.getId();
            String uuid = (String) session.getAttribute("phenotype_uuid");
            System.out.println("Phenotype: " + uuid);
            System.out.println("Name: " + name);
            System.out.println("Score " + score);
            new Goal(uuid, name, session_id, score, this.sql);
        }
        return "{\"code\": \"OK\"}";
    }
}
