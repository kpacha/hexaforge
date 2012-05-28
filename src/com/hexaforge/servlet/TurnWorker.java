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
			return;
		}
		Key gameEntityKey = KeyFactory.createKey(
				GameEntity.class.getSimpleName(), id);
		EntityManager entityManager = EMF.getEntityManager();
		GameInterface game;
		GameEntity gameEntity;
		try {
			gameEntity = (GameEntity) entityManager.find(GameEntity.class,
					gameEntityKey);
			game = JsonDecorator.getInstance().deserializeGame(
					gameEntity.getGame());
		} catch (Exception e) {
			LOGGER.warning("TurnWorker datastore reader error! "
					+ e.getMessage());
			return;
		}
		long now = (new Date()).getTime();
		if (now < game.getNextCheck()) {
			LOGGER.warning("TurnWorker error: " + (game.getNextCheck() - now)
					+ "ms. in advance!");
			return;
		}
		if (game.addTurns(game.getPreferences().getDeltaTurn())) {
			game.setNextCheck(game.getNextCheck()
					+ game.getPreferences().getEtaTurn());
		} else {
			LOGGER.warning("TurnWorker unknown error!");
			return;
		}
		try {
			gameEntity.setGame(JsonDecorator.getInstance().serializeGame(game));
			gameEntity.setNextCheck(game.getNextCheck());
			gameEntity.setStatus(game.getStatus());

			entityManager.persist(gameEntity);
		} catch (Exception e) {
			LOGGER.warning("TurnWorker datastore writer error! "
					+ e.getMessage());
		}
	}
}