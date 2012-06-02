package com.hexaforge.servlet;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.hexaforge.controller.GameController;
import com.hexaforge.util.EMF;

@SuppressWarnings("serial")
public class TurnChecker extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(TurnChecker.class
	    .getName());

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
	    throws IOException {
	long now = (new Date()).getTime();
	LOGGER.info("TurnChecker started at " + now);
	try {
	    GameController controller = new GameController(
		    EMF.getEntityManager());
	    @SuppressWarnings("unchecked")
	    List<String> gameIds = controller.getGameIdsToCheck(now);
	    for (String gameId : gameIds) {
		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(withUrl("/worker/return").param("id", gameId));
		LOGGER.info("Message enqueued: /worker/return with id: "
			+ gameId);
	    }
	} catch (Exception e) {
	    LOGGER.warning(e.getMessage());
	}
    }
}
