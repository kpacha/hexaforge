package com.hexaforge.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.hexaforge.controller.GameController;
import com.hexaforge.util.EMF;

public class HexagameServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
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

	GameController controller;
	try {
	    controller = new GameController(EMF.getEntityManager());
	    user = checkUser(req, resp);
	    controller.setGameEntity(pid);
	    controller.doAction(action, user, req.getParameterMap());
	    resp.getWriter().println(controller.getSerializedGame());
	} catch (Exception e) {
	    System.out.println("doPost Error: " + e.getMessage());
	    resp.getWriter().println(e.getMessage());
	}
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
	    throws IOException {
	String pid = req.getParameter("pid");
	if (pid == null) {
	    System.out.println("Ending incomplete request");
	    return;
	}
	GameController controller;
	try {
	    controller = new GameController(EMF.getEntityManager());
	    controller.setGameEntity(pid);
	    String serializedGame = controller.getSerializedGame();
	    resp.getWriter().println(serializedGame);
	} catch (Exception e) {
	    System.out.println("doGet Error: " + e.getMessage());
	    e.printStackTrace();
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
