package com.hexaforge.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;
import com.hexaforge.entity.GameEntity;
import com.hexaforge.util.PMF;

@SuppressWarnings("serial")
public class TurnChecker extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(GameEntity.class);
		query.setFilter("state == 3 && nextCheck <= nextCheckParam");
		query.setOrdering("nextCheck asc");
		query.declareParameters("long nextCheckParam");
		long now = (new Date()).getTime();

		System.out.println("TurnChecker started at " + now);

		List<GameEntity> results = (List<GameEntity>) query.execute(now);
		try {
			if (results.iterator().hasNext()) {
				for (GameEntity game : results) {
					System.out.println("Checking game --- Now: " + now + ". Next check: "
							+ game.getNextCheck() + ". Diff: "
							+ (now - game.getNextCheck()) + "\n");
					if (now - game.getNextCheck() >= 0) {
						Queue queue = QueueFactory.getDefaultQueue();
						queue.add(withUrl("/worker/return").param("id",
								game.getId()));
						System.out.println("Message enqueued: /worker/return with id: "
								+ game.getId());
					} else {
						System.out.println("Bad result! nextCheck "
								+ game.getNextCheck() + " ("
								+ (now - game.getNextCheck()) + ").");
					}
				}
			} else {
				System.out.println("No results.");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			query.closeAll();
		}
	}

}
