package com.hexaforge.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.hexaforge.core.Game;
import com.hexaforge.util.PMF;

@SuppressWarnings("serial")
public class TurnWorker extends HttpServlet {
	
	private static final Logger log =
	    Logger.getLogger(TurnWorker.class.getName());

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
		Key k = KeyFactory.createKey(Game.class.getSimpleName(), id);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Game battle;
		try {
			battle = pm.getObjectById(Game.class, k);
		} catch (Exception e) {
			log.warning("TurnWorker datastore reader error!");
			return;
		}
		long now = (new Date()).getTime();
		if (now < battle.getChecked()) {
			log.warning("TurnWorker error: " + (battle.getChecked() - now) + "ms. in advance!");
			return;
		}
		if (battle.addTurns(Game.DELTA_TURN)) {
			battle.setChecked(battle.getChecked() + Game.ETA_TURN);
		} else {
			log.warning("TurnWorker unknown error!");
			return;
		}
		try {
			pm.makePersistent(battle);
		} catch (Exception e) {
			log.warning("TurnWorker datastore writer error!");
			return;
		}
		pm.close();
	}
}
