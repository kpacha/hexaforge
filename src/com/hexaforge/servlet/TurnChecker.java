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
import com.hexaforge.core.Battle;
import com.hexaforge.util.PMF;

@SuppressWarnings("serial")
public class TurnChecker extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Battle.class);
		query.setFilter("state == 3 && nextCheck <= nextCheckParam");
		query.setOrdering("nextCheck asc");
		query.declareParameters("long nextCheckParam");
		long now = (new Date()).getTime();

		List<Battle> results = (List<Battle>) query.execute(now);
		try {
			if (results.iterator().hasNext()) {
				// System.out.print("Resultados obtenidos!. Total: "+results.size()+"\n");
				// // testing
				// System.out.print("Resultados obtenidos!.\n"); // testing
				for (Battle b : results) {
					if (now - b.getChecked() >= 0) {
						Queue queue = QueueFactory.getDefaultQueue();
						queue.add(withUrl("/worker/return").param("id",
								b.getId()));
						// System.out.print("Añadido "+b.getId()+" en la queue.\n");
						// // testing
						// System.out.print("nextCheck "+b.getChecked()+" ("+(now-b.getChecked())+").\n");
						// // testing
					} else {
						// System.out.print("Resultado indeseado! nextCheck "+b.getChecked()+" ("+(now-b.getChecked())+").\n");
						// // testing
					}
				}
			} else {
				// System.out.print("Tarea programada: no hay resultados.\n");
				// // testing
			}
		} catch (Exception e) {
			// System.out.print("Error en la tarea programada: "+e.getMessage()+".\n");
			// // testing
		} finally {
			query.closeAll();
			// System.out.print("Umbral: "+(new Date()).getTime()+".\n"); //
			// testing
		}
	}

}
