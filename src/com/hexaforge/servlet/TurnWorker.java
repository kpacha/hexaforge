package com.hexaforge.servlet;

import java.io.IOException;
import java.util.Date;

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
		// System.out.print("Consumiendo "+id+" de la queue.\n"); // testing
		Key k = KeyFactory.createKey(Game.class.getSimpleName(), id);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Game battle;
		try {
			battle = pm.getObjectById(Game.class, k);
		} catch (Exception e) {
			// System.out.print("error recuperando partida del datastore: "+id+"\n");
			// // testing
			return;
		}
		if (((new Date()).getTime() - battle.getChecked()) < 0) {
			// System.out.print("Worker llamado innecesariamente para el id:"+id+"\n");
			// // testing
			return;
		}
		if (battle.addTurns(Game.DELTA_TURN)) {
			battle.setChecked((new Date()).getTime() + Game.ETA_TURN);
		} else {
			// System.out.print("error incrementando los turnos de: "+id+"\n");
			// // testing
			return;
		}
		try {
			pm.makePersistent(battle);
		} catch (Exception e) {
			// System.out.print("error recuperando partida del datastore: "+id+"\n");
			// // testing
			return;
		}
		pm.close();
		// System.out.print("Consumido "+battle.getId()+" de la queue.\n"); //
		// testing
	}
}
