package ai.effect.servlet.dna;

import javax.servlet.http.HttpServletRequest;

import ai.effect.server.SqlHandler;

public class GoalServlet extends DnaServlet {
    public GoalServlet(SqlHandler sql) {
        super(sql);
    }

    protected String getResponse(HttpServletRequest req, String[] arguments) {
        System.out.println(arguments[1]);
        System.out.println(arguments[2]);
        return "{\"code\": \"OK\"}";
    }
}
