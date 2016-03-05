package ai.effect.servlet.dna;

import javax.servlet.http.HttpServletRequest;

import ai.effect.server.SqlHandler;

public class ClickServlet extends DnaServlet {
    public ClickServlet(SqlHandler sql) {
        super(sql);
    }

    protected String getResponse(HttpServletRequest req, String argument) {
        return "{\"code\": \"OK\"}";
    }
}
