package com.hexaforge.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.hexaforge.core.decorator.JsonDecorator;
import com.hexaforge.core.interfaces.GameInterface;
import com.hexaforge.entity.GameEntity;
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
		Key gameEntityKey = KeyFactory.createKey(
				GameEntity.class.getSimpleName(), id);
		EntityManager entityManager = EMF.getEntityManager();
		GameInterface game;
		GameEntity gameEntity;
		JsonDecorator game2Json = JsonDecorator.getInstance();
		try {
			gameEntity = (GameEntity) entityManager.find(GameEntity.class,
					gameEntityKey);
			game = game2Json.deserializeGame(gameEntity.getGame());
			LOGGER.info("TurnWorker has received the game from the datastore!");
		} catch (Exception e) {
			LOGGER.warning("TurnWorker datastore reader error! "
					+ e.getMessage());
			entityManager.close();
			return;
		}

		long now = (new Date()).getTime();
		LOGGER.info("Checking game --- Now: " + now + ". Next check: "
				+ gameEntity.getNextCheck() + ". Diff: "
				+ (now - gameEntity.getNextCheck()));

		if (now < game.getNextCheck()) {
			LOGGER.warning("TurnWorker error: " + (game.getNextCheck() - now)
					+ "ms. in advance!");
			entityManager.close();
			return;
		}

		long nextCheck = game.getNextCheck();
		if (game.addTurns(game.getPreferences().getDeltaTurn())) {
			nextCheck += game.getPreferences().getEtaTurn();
			game.setNextCheck(nextCheck);
			LOGGER.info("Updating internal checking time --- Now: " + now
					+ ". Next check: " + nextCheck + ". Diff: "
					+ (now - nextCheck));
		} else {
			LOGGER.warning("TurnWorker unknown error!");
			entityManager.close();
			return;
		}

		try {
			gameEntity.setGame(game2Json.serializeGame(game));
			gameEntity.setNextCheck(nextCheck);
			gameEntity.setStatus(game.getStatus());

			entityManager.persist(gameEntity);
			LOGGER.info("Updating external checking time --- Now: " + now
					+ ". Next check: " + gameEntity.getNextCheck() + ". Diff: "
					+ (now - gameEntity.getNextCheck()));
		} catch (Exception e) {
			LOGGER.warning("TurnWorker datastore writer error! "
					+ e.getMessage());
		} finally {
			entityManager.close();
		}
	}
}