package ai.effect.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.eclipse.jetty.http.HttpStatus;

public class InitServlet extends DnaServlet {
    protected String getResponse(HttpServletRequest req) {
        HttpSession session = req.getSession(true);

        if (session == null) {
            System.out.println("no session");
        } else {
            session.setAttribute("created", new Date());
            session.setAttribute("ip", req.getRemoteAddr());
            session.setAttribute("start", "");
            session.setAttribute("stop", "");
            System.out.println("Session = " + session.getId());
            System.out.println("Created = " + session.getAttribute("created"));
            System.out.println("Ip = " + session.getAttribute("ip"));
            System.out.println("Start = " + session.getAttribute("start"));
            System.out.println("Stop = " + session.getAttribute("stop"));
        }
        return "{\"session-id\": " + session.getId()
                + ", \"items\": [{\"id\": \".btn\", \"attributes\": [{\"attribute\": \"background-color\", \"value\": \"blue\"}]}]}";
    }
}
