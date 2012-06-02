package com.hexaforge.command;

import java.util.Map;

import javax.persistence.EntityManager;

import com.google.appengine.api.users.User;
import com.hexaforge.command.exception.JoinException;
import com.hexaforge.core.interfaces.GameInterface;

public class JoinCommand extends AbstractCommand {

    public JoinCommand(EntityManager entityManager) {
	super(entityManager);
    }

    protected GameInterface doExecute(Map<String, String[]> parameters,
	    User user) throws Exception {
	GameInterface game = game2Json.deserializeGame(gameEntity.getGame());
	if (!game.addPlayer(user.getNickname()))
	    throw new JoinException();
	gameDAO.persist(game, gameEntity);
	return game;
    }
}