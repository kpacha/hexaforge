package com.hexaforge.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;
import com.hexaforge.core.Game;
import com.hexaforge.util.PMF;

@SuppressWarnings("serial")
public class TurnChecker extends HttpServlet {
	
	private static final Logger log =
	    Logger.getLogger(TurnChecker.class.getName());
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Game.class);
		query.setFilter("state == 3 && nextCheck <= nextCheckParam");
		query.setOrdering("nextCheck asc");
		query.declareParameters("long nextCheckParam");
		long now = (new Date()).getTime();
		
		log.warning("TurnChecker started at " + now);

		List<Game> results = (List<Game>) query.execute(now);
		try {
			if (results.iterator().hasNext()) {
				for (Game game : results) {
					log.info("Checking game --- Now: " + now + ". Next check: " + game.getNextCheck() + ". Diff: " + (now - game.getNextCheck()) + "\n");
					if (now - game.getNextCheck() >= 0) {
						Queue queue = QueueFactory.getDefaultQueue();
						queue.add(withUrl("/worker/return").param("id",
								game.getId()));
						log.info("Message enqueued: /worker/return with id: " + game.getId());
					} else {
						log.warning("Bad result! nextCheck "+game.getNextCheck()+" ("+(now-game.getNextCheck())+").");
					}
				}
			} else {
				log.info("No results.");
			}
		} catch (Exception e) {
			log.warning(e.getMessage());
		} finally {
			query.closeAll();
		}
	}

}
