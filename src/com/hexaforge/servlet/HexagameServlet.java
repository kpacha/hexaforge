package com.hexaforge.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.hexaforge.controller.GameController;
import com.hexaforge.core.ExceptionMessage;
import com.hexaforge.core.decorator.JsonDecorator;
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
	String serializedResponse = null;
	try {
	    GameController controller = new GameController(
		    EMF.getEntityManager(), req.getParameterMap());
	    user = checkUser(req, resp);
	    serializedResponse = controller.doAction(user);
	} catch (Exception e) {
	    JsonDecorator gson = JsonDecorator.getInstance();
	    serializedResponse = gson.serializeException(new ExceptionMessage(e
		    .getMessage()));
	}
	returnJson(serializedResponse, resp);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
	    throws IOException {
	String pid = req.getParameter("pid");
	if (pid == null) {
	    System.out.println("Ending incomplete request");
	    return;
	}
	String serializedResponse = null;
	try {
	    Map<String, String[]> parameterMap = new HashMap<String, String[]>();
	    parameterMap.putAll(req.getParameterMap());
	    String[] aid = { "view" };
	    parameterMap.put("aid", aid);
	    GameController controller = new GameController(
		    EMF.getEntityManager(), parameterMap);
	    serializedResponse = controller.doAction(null);
	} catch (Exception e) {
	    JsonDecorator gson = JsonDecorator.getInstance();
	    serializedResponse = gson.serializeException(new ExceptionMessage(e
		    .getMessage()));
	}
	returnJson(serializedResponse, resp);
    }

    private User checkUser(HttpServletRequest req, HttpServletResponse resp)
	    throws Exception {
	UserService userService = UserServiceFactory.getUserService();
	User user = userService.getCurrentUser();
	if (user == null)
	    throw new Exception("Client is not logged in");
	return user;
    }

    private void returnJson(String serializedResponse, HttpServletResponse resp)
	    throws IOException {
	resp.setContentType("text/x-json");
	resp.getWriter().println(serializedResponse);
    }
}
