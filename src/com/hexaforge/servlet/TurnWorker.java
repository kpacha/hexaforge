package com.hexaforge.servlet;

import java.io.IOException;
import java.util.Date;

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
		Key k = KeyFactory.createKey(GameEntity.class.getSimpleName(), id);
		EntityManager entityManager = EMF.getEntityManager();
		GameInterface game;
		GameEntity gameEntity;
		try {
			gameEntity = (GameEntity) entityManager.find(GameEntity.class, k);
			game = JsonDecorator.getInstance().deserializeGame(
					gameEntity.getGame());
		} catch (Exception e) {
			System.out.println("TurnWorker datastore reader error!");
			return;
		}
		long now = (new Date()).getTime();
		if (now < game.getNextCheck()) {
			System.out.println("TurnWorker error: "
					+ (game.getNextCheck() - now) + "ms. in advance!");
			return;
		}
		if (game.addTurns(game.getPreferences().getDeltaTurn())) {
			game.setNextCheck(game.getNextCheck()
					+ game.getPreferences().getEtaTurn());
		} else {
			System.out.println("TurnWorker unknown error!");
			return;
		}
		try {
			gameEntity.setGame(JsonDecorator.getInstance().serializeGame(game));
			gameEntity.setNextCheck(game.getNextCheck());
			gameEntity.setStatus(game.getStatus());

			entityManager.persist(game);
		} catch (Exception e) {
			System.out.println("TurnWorker datastore writer error!");
		}
	}
}