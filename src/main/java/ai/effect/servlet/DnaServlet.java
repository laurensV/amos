package ai.effect.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;

public abstract class DnaServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		resp.setStatus(HttpStatus.OK_200);
		String response = this.getResponse();
		resp.getWriter().println(response);
	}
	
	/**
	 * This response is returned to client on success.
	 * @return
	 */
	protected abstract String getResponse();
}