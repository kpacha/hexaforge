package com.hexaforge.command;

import java.util.Map;

import javax.persistence.EntityManager;

import com.google.appengine.api.users.User;
import com.hexaforge.core.Game;
import com.hexaforge.core.GamePreferences;
import com.hexaforge.core.interfaces.GameInterface;

public class NewGameCommand extends AbstractCommand {

    public NewGameCommand(EntityManager entityManager) {
	super(entityManager);
    }

    protected GameInterface doExecute(Map<String, String[]> parameters,
	    User user) throws Exception {
	int initialDeltaTurn = parameter2Int("idt", parameters);
	int deltaTrun = parameter2Int("dt", parameters);
	int etaTurn = parameter2Int("et", parameters);
	String boardType = parameters.get("bt")[0];
	int sizeX = parameter2Int("sx", parameters);
	int sizeY = parameter2Int("sy", parameters);

	GameInterface game = new Game(new GamePreferences(initialDeltaTurn,
		deltaTrun, etaTurn, boardType, sizeX, sizeY));
	game.addPlayer(user.getNickname());
	gameDAO.persist(game, gameEntity);
	return game;
    }
}