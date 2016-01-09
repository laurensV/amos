package ai.effect.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InitServlet extends DnaServlet {
	protected String getResponse() {
		return "{\"session-id\": 223423423423, \"items\": [{\"id\": \".btn\", \"attributes\": [{\"attribute\": \"background-color\", \"value\": \"blue\"}]}]}";
	}
}
