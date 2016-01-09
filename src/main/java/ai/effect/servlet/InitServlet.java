package ai.effect.servlet;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class InitServlet extends DnaServlet {
    protected String getResponse(HttpServletRequest req, String argument) {
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
        String siteId = argument;
        /* TODO: use IP and current time to map user to a profile */
        /* TODO: use siteId and profile to select DNA */
        return "{\"session-id\": \"" + session.getId()
                + "\", \"items\": [{\"id\": \".btn\", \"attributes\": [{\"attribute\": \"background-color\", \"value\": \"blue\"}]}]}";
    }
}
