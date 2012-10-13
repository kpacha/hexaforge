package com.hexaforge.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hexaforge.controller.GameController;
import com.hexaforge.util.EMF;

@SuppressWarnings("serial")
public class TurnWorker extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(TurnWorker.class
	    .getName());

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
	    throws IOException {
	doPost(req, resp);
    }

    @SuppressWarnings("unchecked")
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
	    throws IOException {
	String id = req.getParameter("pid");
	if (id == null) {
	    LOGGER.warning("TurnWorker has not received a game id!");
	    return;
	}
	try {
	    Map<String, String[]> parameterMap = new HashMap<String, String[]>();
	    parameterMap.putAll(req.getParameterMap());
	    String[] aid = { "updateTurns" };
	    parameterMap.put("aid", aid);
	    GameController controller = new GameController(
		    EMF.getEntityManager(), parameterMap);
	    controller.doAction(null);
	    LOGGER.warning("TurnWorker game [" + id + "] updated!");
	} catch (Exception e) {
	    LOGGER.warning("TurnWorker gets an error! " + e.getMessage());
	}
    }
}