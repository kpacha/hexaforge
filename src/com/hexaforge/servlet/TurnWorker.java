package com.hexaforge.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;
import com.hexaforge.core.Battle;
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
		Key k = KeyFactory.createKey(Battle.class.getSimpleName(), id);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Battle battle;
		try {
			battle = pm.getObjectById(Battle.class, k);
		} catch (Exception e) {
			// System.out.print("error recuperando partida del datastore: "+id+"\n");
			// // testing
			return;
		}
		long dif;
		if ((dif = (new Date()).getTime() - battle.getChecked()) < 0) {
			// System.out.print("Worker llamado innecesariamente para el id:"+id+", dif="+dif+"\n");
			// // testing
			return;
		}
		if (battle.addTurns(Battle.DELTA_TURN)) {
			battle.setChecked((new Date()).getTime() + Battle.ETA_TURN);
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
