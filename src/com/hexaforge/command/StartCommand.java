package com.hexaforge.command;

import java.util.Map;

import javax.persistence.EntityManager;

import com.google.appengine.api.users.User;
import com.hexaforge.command.exception.StartException;
import com.hexaforge.core.interfaces.GameInterface;

public class StartCommand extends AbstractCommand {

    public StartCommand(EntityManager entityManager) {
	super(entityManager);
    }

    protected GameInterface doExecute(Map<String, String[]> parameters,
	    User user) throws Exception {
	GameInterface game = game2Json.deserializeGame(gameEntity.getGame());
	if (!game.startGame())
	    throw new StartException();
	gameDAO.persist(game, gameEntity);
	return game;
    }
}