package ai.effect.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StopServlet extends DnaServlet {
	protected String getResponse() {
		return "Click";
	}
}
