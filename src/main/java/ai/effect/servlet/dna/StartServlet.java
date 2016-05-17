package ai.effect.servlet.dna;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ai.effect.server.SqlHandler;

public class StartServlet extends DnaServlet {
    public StartServlet(SqlHandler sql) {
        super(sql);
    }

    protected String getResponse(HttpServletRequest req, String[] arguments) {
        HttpSession session = req.getSession(false);

        if (session == null) {
            System.out.println("no session");
        } else {
            session.setAttribute("start", new Date());
            System.out.println("Session = " + session.getId());
            System.out.println("Created = " + session.getAttribute("created"));
            System.out.println("Ip = " + session.getAttribute("ip"));
            System.out.println("Start = " + session.getAttribute("start"));
            System.out.println("Stop = " + session.getAttribute("stop"));
        }
        return "{\"code\": \"OK\"}";
    }
}
