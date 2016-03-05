package ai.effect.servlet.dna;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;

import ai.effect.server.SqlHandler;
import ai.effect.servlet.Servlet;

public abstract class DnaServlet extends Servlet {
    public DnaServlet(SqlHandler sql) {
        super(sql);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String argument = "";
        resp.setStatus(HttpStatus.OK_200);
        /* TODO: Security checks for user input argument */
        if (req.getPathInfo() != null) {
            argument = req.getPathInfo().split("/")[1];
        }
        String response = this.getResponse(req, argument);
        resp.getWriter().println(response);
    }

    /**
     * This response is returned to client on success.
     * 
     * @return
     */
    protected abstract String getResponse(HttpServletRequest req, String argument);
}
