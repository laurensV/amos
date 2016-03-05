package ai.effect.servlet.dna;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.eclipse.jetty.http.HttpStatus;

public class StartServlet extends DnaServlet {
    protected String getResponse(HttpServletRequest req, String argument) {
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
