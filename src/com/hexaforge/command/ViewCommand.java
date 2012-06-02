package com.hexaforge.command;

import java.util.Map;

import javax.persistence.EntityManager;

import com.google.appengine.api.users.User;
import com.hexaforge.core.interfaces.GameInterface;

public class ViewCommand extends AbstractCommand {

    public ViewCommand(EntityManager entityManager) {
	super(entityManager);
    }

    protected GameInterface doExecute(Map<String, String[]> parameters,
	    User user) throws Exception {
	return game2Json.deserializeGame(gameEntity.getGame());
    }
}