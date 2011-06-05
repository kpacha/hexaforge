package com.hexaforge.servlet;

import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.servlet.http.*;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.hexaforge.core.Game;
import com.hexaforge.util.PMF;

@SuppressWarnings("serial")
public class HexagameServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		User user;
		if ((user = checkUser(req, resp)) == null) {
			return;
		}
		String accion = req.getParameter("aid");
		String pid = req.getParameter("pid");
		if (pid == null && accion != null && accion.equalsIgnoreCase("new")) {
			// System.out.print("creando partida\n");
			PersistenceManager pm = PMF.get().getPersistenceManager();
			Game g = new Game();
			resp.setContentType("text/plain");
			resp.getWriter().println(
					"Partida creada. Link invitación: " + g.getId());
			if (g.addPlayer(user.getUserId(), user.getNickname(), 0)) {
				pm.makePersistent(g);
			}
			pm.close();
			return;
		}
		if (pid != null) {
			// System.out.print("redireccionando post vacío\n");
			Key k = KeyFactory.createKey(Game.class.getSimpleName(), pid);
			PersistenceManager pm = PMF.get().getPersistenceManager();
			Game battle;
			try {
				battle = pm.getObjectById(Game.class, k);
				// System.out.print("partida recuperada del datastore: "+pid+"\n");
				resp.setContentType("text/x-json");
				publishJSONBoard(req, resp, battle);
				return;
			} catch (Exception e) {
				// System.out.print("error recuperando partida del datastore: "+pid+"\n");
				return;
			}
		}
		resp.setContentType("text/plain");
		resp.getWriter().println("Listado partidas activas del jugador");
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		User user;
		if ((user = checkUser(req, resp)) == null) {
			return;
		}
		resp.setContentType("text/x-json");
		String accion = req.getParameter("aid");
		if (accion == null) {
			// System.out.print("redireccionando petición incompleta\n"); 
			doGet(req, resp);
			return;
		}
		String pid = req.getParameter("pid");
		if (pid == null && accion.equalsIgnoreCase("new")) {
			// System.out.print("redireccionando post vacío\n");
			doGet(req, resp);
			return;
		}
		Key k = KeyFactory.createKey(Game.class.getSimpleName(), pid);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Game battle;
		try {
			battle = pm.getObjectById(Game.class, k);
			// System.out.print("partida recuperada del datastore: "+pid+"\n");
		} catch (Exception e) {
			// System.out.print("error recuperando partida del datastore: "+pid+"\n");
			return;
		}
		// System.out.print("Gestión de la acción <" +accion+
		// "> para la partida " +pid+"\n");
		if (accion.equalsIgnoreCase("join")) {
			if (battle.addPlayer(user.getUserId(), user.getNickname())) {
				pm.makePersistent(battle);
			}
		} else if (accion.equalsIgnoreCase("quit")) {
			if (battle.delPlayer(user.getNickname())) {
				pm.makePersistent(battle);
				resp.getWriter().println(
						"Te has retirado de la partida " + battle.getId());
				return;
			}
		} else if (accion.equalsIgnoreCase("start")) {
			if (battle.startGame()) {
				pm.makePersistent(battle);
			}
		} else if (accion.equalsIgnoreCase("move")) {
			String movementString = req.getParameter("m");
			if (movementString == null) {
				publishJSONBoard(req, resp, battle);
				return;
			}
			if (battle.move(movementString)) {
				pm.makePersistent(battle);
			}
		}
		pm.close();
		publishJSONBoard(req, resp, battle);
	}

	private User checkUser(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if (user == null) {
			resp.getWriter().println(
					"<p>Please <a href=\"" + userService.createLoginURL("/")
							+ "\">sign in</a>.</p>");
		}
		return user;
	}

	private void publishJSONBoard(HttpServletRequest req,
			HttpServletResponse resp, Game g) throws IOException {
		// String result =
		// "{\"turno\":"+b.getTurn()+",\"jugadores\":"+b.getPlayers()+",\"tablero\":"+b.getBoard()+"}";
		resp.getWriter()
				.println(
						"{\"turno\":" + g.getTurn() + ",\"jugadores\":"
								+ g.getPlayers() + ",\"tablero\":"
								+ g.getBoard() + "}");
	}
}
