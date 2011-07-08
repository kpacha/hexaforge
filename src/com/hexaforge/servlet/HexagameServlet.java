package com.hexaforge.servlet;

import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.servlet.http.*;

import org.datanucleus.exceptions.NucleusObjectNotFoundException;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.hexaforge.controller.GameController;
import com.hexaforge.core.Game;
import com.hexaforge.core.GamePreferences;
import com.hexaforge.util.Channel;
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
			doPost(req, resp);
			return;			
		}
		if (pid != null) {
			// System.out.print("mostrando partida\n");
			showGame(req, resp, pid);
			return;
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
			GameController controller = new GameController(game, pm);
			if(!controller.execute(accion, user, req.getParameter("m"))){
				resp.sendRedirect("/error.html");
				return;
			}else{
				if(accion.equalsIgnoreCase("start")){
					resp.sendRedirect("/tablero.html?pid=" + game.getId());
				}
			}
		}catch(NucleusObjectNotFoundException e){
			resp.sendRedirect("/error.html");
			System.out.print("\nError en la recuperación de la partida : "
					+ e.getMessage() +"\n");
			return;
		}catch(Exception e){
			resp.sendRedirect("/error.html");
			System.out.print("\nError en la utilización del GameController: "
					+ e.getMessage() +"\n");
			System.out.print("\nmás info: "
					+ e.getCause() +"\n");
			System.out.print("\nm: "
					+ req.getParameter("m") +"\n");
			return;
		}finally{
			pm.close();
		}		
		game.sendUpdateToClients();
		resp.getWriter().println(game.toString());
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
	
	private void newGame(HttpServletRequest req, HttpServletResponse resp, User user)
			throws IOException {
		String i, d, e;
		i = req.getParameter("initialDeltaTurn");
		d = req.getParameter("deltaTurn");
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
	
	private void showGame(HttpServletRequest req, HttpServletResponse resp, String pid)
			throws IOException {
		Key k = KeyFactory.createKey(Game.class.getSimpleName(), pid);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Game game;
		try {
			game = pm.getObjectById(Game.class, k);
			// System.out.print("partida recuperada del datastore: "+pid+"\n");
			resp.setContentType("text/x-json");
			resp.getWriter().println(game.toString());
			return;
		} catch (Exception e) {
			// System.out.print("error recuperando partida del datastore: "+pid+"\n");
			return;
		}
	}

}
