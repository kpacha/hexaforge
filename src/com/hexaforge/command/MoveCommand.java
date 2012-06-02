package com.hexaforge.command;

import java.util.Map;

import javax.persistence.EntityManager;

import com.google.appengine.api.users.User;
import com.hexaforge.command.exception.MovementException;
import com.hexaforge.core.interfaces.GameInterface;

public class MoveCommand extends AbstractCommand {

    public MoveCommand(EntityManager entityManager) {
	super(entityManager);
    }

    protected GameInterface doExecute(Map<String, String[]> parameters,
	    User user) throws Exception {
	int fromX = parameter2Int("sx", parameters);
	int fromY = parameter2Int("sy", parameters);
	int toX = parameter2Int("tx", parameters);
	int toY = parameter2Int("ty", parameters);
	int turn = parameter2Int("turn", parameters);
	GameInterface game = game2Json.deserializeGame(gameEntity.getGame());
	if (!game.move(user.getNickname(), turn, fromX, fromY, toX, toY))
	    throw new MovementException();
	gameDAO.persist(game, gameEntity);
	return game;
    }
}