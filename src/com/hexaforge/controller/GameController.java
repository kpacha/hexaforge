package com.hexaforge.controller;

import java.util.Map;

import javax.persistence.EntityManager;

import com.google.appengine.api.users.User;
import com.hexaforge.command.AbstractCommand;
import com.hexaforge.command.CommandFactory;

public class GameController {

    private Map<String, String[]> parameterMap;
    private String action;
    private EntityManager entityManager;

    public GameController(EntityManager entityManager,
	    Map<String, String[]> parameterMap) {
	this.entityManager = entityManager;
	this.parameterMap = parameterMap;
	this.action = parameterMap.get("aid")[0];
    }

    public AbstractCommand getCommand() throws Exception {
	return CommandFactory.getCommand(action, entityManager);
    }

    public String doAction(User user) throws Exception {
	String game = null;
	try {
	    AbstractCommand command = getCommand();
	    game = command.execute(parameterMap, user);
	} catch (Exception e) {
	    System.out.println("Exception throwed!: " + e.getMessage());
	    throw e;
	}
	return game;
    }
}