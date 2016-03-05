package ai.effect.servlet;

import javax.servlet.http.HttpServlet;

import ai.effect.server.SqlHandler;

public abstract class Servlet extends HttpServlet {
    protected SqlHandler sql;

    /**
     * Constructor
     */
    public Servlet(SqlHandler sql) {
        this.sql = sql;
    }
}
