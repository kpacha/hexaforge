package com.hexaforge.servlet;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.hexaforge.controller.GameController;
import com.hexaforge.entity.GameEntity;
import com.hexaforge.util.EMF;

public class HexagameServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		User user;
		String action = req.getParameter("aid");
		if (action == null) {
			System.out.println("Redirecting incomplete request");
			doGet(req, resp);
			return;
		}
		String pid = req.getParameter("pid");

		EntityManager em = EMF.getEntityManager();
		GameController controller;
		try {
			controller = new GameController(em);
			user = checkUser(req, resp);
			controller.setGameEntity(pid);
			if (controller.doAction(action, user, req.getParameterMap()))
				resp.getWriter().println(controller.getUpdatedSerializedGame());
		} catch (Exception e) {
			System.out.println("doPost Error: " + e.getMessage());
			e.printStackTrace();
		} finally {
			em.close();
		}
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String pid = req.getParameter("pid");
		if (pid == null) {
			System.out.println("Ending incomplete request");
			return;
		}
		EntityManager em = EMF.getEntityManager();
		try {
			GameEntity ge = em
					.find(GameEntity.class,
							KeyFactory.createKey(
									GameEntity.class.getSimpleName(), pid));
			if (ge == null) {
				System.out.println("Unknown gid " + pid);
				return;
			}
			resp.getWriter().print(ge.getGame());
		} catch (Exception e) {
			System.out.println("doGet Error: " + e.getMessage());
			e.printStackTrace();
		} finally {
			em.close();
		}
	}

	private User checkUser(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if (user == null)
			throw new Exception("Client is not logged in");
		return user;
	}
}
