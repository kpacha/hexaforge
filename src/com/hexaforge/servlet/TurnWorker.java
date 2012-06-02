package com.hexaforge.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hexaforge.controller.GameController;
import com.hexaforge.util.EMF;

@SuppressWarnings("serial")
public class TurnWorker extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(TurnChecker.class
	    .getName());

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
	    throws IOException {
	doPost(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
	    throws IOException {
	String id = req.getParameter("id");
	if (id == null) {
	    LOGGER.warning("TurnWorker has not received a game id!");
	    return;
	}
	try {
	    GameController controller = new GameController(
		    EMF.getEntityManager());
	    controller.setGameEntity(id);
	    controller.updateTurns((new Date()).getTime());
	    LOGGER.warning("TurnWorker game [" + id + "] updated!");
	} catch (Exception e) {
	    LOGGER.warning("TurnWorker gets an error! " + e.getMessage());
	}
    }
}