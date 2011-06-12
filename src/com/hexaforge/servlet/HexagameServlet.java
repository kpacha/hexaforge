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
import com.hexaforge.core.GamePreferences;
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
			// System.out.print("redireccionando al formulario de preferencias");
			resp.sendRedirect("/preferences.jsp");			
		}
		if (pid != null && accion != null && accion.equalsIgnoreCase("join")) {
			// System.out.print("uniendo a la partida");
			doGet(req, resp);
			return;			
		}
		if (pid != null) {
			// System.out.print("mostrando partida\n");
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
		// System.out.print("redireccionando al listado de partidas\n");
		resp.sendRedirect("/games.jsp");
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
			newGame(req, resp, user);
			return;
		}
		Key k = KeyFactory.createKey(Game.class.getSimpleName(), pid);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Game game;
		try {
			game = pm.getObjectById(Game.class, k);
			// System.out.print("partida recuperada del datastore: "+pid+"\n");
		} catch (Exception e) {
			// System.out.print("error recuperando partida del datastore: "+pid+"\n");
			return;
		}
		// System.out.print("Gestión de la acción <" +accion+
		// "> para la partida " +pid+"\n");
		if (accion.equalsIgnoreCase("join")) {
			if (game.addPlayer(user.getUserId(), user.getNickname())) {
				pm.makePersistent(game);
			}
		} else if (accion.equalsIgnoreCase("quit")) {
			if (game.delPlayer(user.getNickname())) {
				pm.makePersistent(game);
				resp.getWriter().println(
						"Te has retirado de la partida " + game.getId());
				return;
			}
		} else if (accion.equalsIgnoreCase("start")) {
			if (game.startGame()) {
				pm.makePersistent(game);
			}
		} else if (accion.equalsIgnoreCase("move")) {
			String movementString = req.getParameter("m");
			if (movementString == null) {
				publishJSONBoard(req, resp, game);
				return;
			}
			if (game.move(movementString)) {
				pm.makePersistent(game);
			}
		}
		pm.close();
		publishJSONBoard(req, resp, game);
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
	
	private void newGame(HttpServletRequest req, HttpServletResponse resp, User user)
			throws IOException {
		String i, d, e;
		i = req.getParameter("initialDeltaTurn");
		d = req.getParameter("deltaTrun");
		e = req.getParameter("etaTurn");
		if(i != null && d != null && e != null){
			PersistenceManager pm = PMF.get().getPersistenceManager();
			GamePreferences prefs = new GamePreferences(
					Integer.parseInt(i),
					Integer.parseInt(d),
					Integer.parseInt(e)
					);
			Game g = new Game(prefs);
			resp.setContentType("text/plain");
			resp.getWriter().println(
					"Partida creada. Link invitación: " + g.getId());
			if (g.addPlayer(user.getUserId(), user.getNickname(), 0)) {
				pm.makePersistent(g);
			}
			pm.close();
		} else {
			// System.out.print("redireccionando al formulario de preferencias");
			resp.sendRedirect("/preferences.jsp");
		}
		return;
	}
}
