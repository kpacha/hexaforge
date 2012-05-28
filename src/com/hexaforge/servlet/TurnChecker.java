package com.hexaforge.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;

import com.hexaforge.entity.GameEntity;
import com.hexaforge.util.EMF;

@SuppressWarnings("serial")
public class TurnChecker extends HttpServlet {

	private static final Logger LOGGER = Logger.getLogger(TurnChecker.class
			.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		EntityManager entityManager = EMF.getEntityManager();
		Query queryGamesToCheck = entityManager
				.createQuery("SELECT game FROM GameEntity game WHERE game.nextCheck <= :nextCheck AND game.status = 3");
		long now = (new Date()).getTime();

		LOGGER.info("TurnChecker started at " + now);
		try {
			queryGamesToCheck.setParameter("nextCheck", now);
			List<GameEntity> games = queryGamesToCheck.getResultList();
			Iterator<GameEntity> iter = games.iterator();
			if (!iter.hasNext()) {
				LOGGER.warning("No results.");
				return;
			}
			while (iter.hasNext()) {
				GameEntity gameEntity = iter.next();
				LOGGER.info("Checking game --- Now: " + now + ". Next check: "
						+ gameEntity.getNextCheck() + ". Diff: "
						+ (now - gameEntity.getNextCheck()));
				if (now - gameEntity.getNextCheck() >= 0) {
					Queue queue = QueueFactory.getDefaultQueue();
					queue.add(withUrl("/worker/return").param("id",
							gameEntity.getId()));
					LOGGER.info("Message enqueued: /worker/return with id: "
							+ gameEntity.getId());
				} else {
					LOGGER.info("Bad result! nextCheck "
							+ gameEntity.getNextCheck() + " ("
							+ (now - gameEntity.getNextCheck()) + ").");
				}
			}
		} catch (Exception e) {
			LOGGER.warning(e.getMessage());
		}
	}
}
