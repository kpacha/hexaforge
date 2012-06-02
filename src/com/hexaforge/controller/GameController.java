package com.hexaforge.controller;

import java.util.Map;

import javax.persistence.EntityManager;

import com.google.appengine.api.users.User;
import com.hexaforge.core.Game;
import com.hexaforge.core.GamePreferences;
import com.hexaforge.core.decorator.JsonDecorator;
import com.hexaforge.core.interfaces.GameInterface;
import com.hexaforge.entity.GameDAO;
import com.hexaforge.entity.GameEntity;

public class GameController {

    private JsonDecorator game2Json;
    private GameEntity gameEntity;
    private GameDAO gameDAO;

    public GameController(EntityManager entityManager) {
	this.gameDAO = new GameDAO(entityManager);
	this.game2Json = JsonDecorator.getInstance();
    }

    public void setGameEntity(String pid) throws Exception {
	if (pid == null) {
	    gameEntity = new GameEntity();
	} else {
	    gameEntity = gameDAO.load(pid);
	}
	if (gameEntity == null) {
	    throw new Exception("Unknown game [" + pid + "]");
	}
    }

    public String getSerializedGame() {
	return gameEntity.getGame();
    }

    public boolean doAction(String action, User user,
	    Map<String, String[]> parameterMap) throws Exception {
	boolean success = false;
	GameInterface game = null;
	try {
	    if (action.equalsIgnoreCase("new")) {
		game = newGame(parameterMap, user);
		success = true;
	    } else {
		game = game2Json.deserializeGame(gameEntity.getGame());
		if (action.equalsIgnoreCase("join"))
		    success = game.addPlayer(user.getNickname());
		else if (action.equalsIgnoreCase("start"))
		    success = game.startGame();
		else if (action.equalsIgnoreCase("move")) {
		    game = move(parameterMap, user, game);
		    success = true;
		} else
		    throw new Exception("Unknown action [" + action + "]");
	    }
	    if (success)
		success = gameDAO.persist(game, gameEntity);
	} catch (Exception e) {
	    System.out.println("Exception throwed!: " + e.getMessage());
	    throw e;
	}
	return success;
    }

    protected GameInterface newGame(Map<String, String[]> parameters, User user)
	    throws Exception {
	int initialDeltaTurn = parameter2Int("idt", parameters);
	int deltaTrun = parameter2Int("dt", parameters);
	int etaTurn = parameter2Int("et", parameters);
	String boardType = parameters.get("bt")[0];
	int sizeX = parameter2Int("sx", parameters);
	int sizeY = parameter2Int("sy", parameters);

	GameInterface game = new Game(new GamePreferences(initialDeltaTurn,
		deltaTrun, etaTurn, boardType, sizeX, sizeY));
	game.addPlayer(user.getNickname());
	return game;
    }

    protected GameInterface move(Map<String, String[]> parameters, User user,
	    GameInterface game) throws Exception {
	int fromX = parameter2Int("sx", parameters);
	int fromY = parameter2Int("sy", parameters);
	int toX = parameter2Int("tx", parameters);
	int toY = parameter2Int("ty", parameters);
	int turn = parameter2Int("turn", parameters);
	if (!game.move(user.getNickname(), turn, fromX, fromY, toX, toY))
	    throw new MovementException();
	return game;
    }

    protected static int parameter2Int(String paramName,
	    Map<String, String[]> parameters) {
	return Integer.parseInt(parameters.get(paramName)[0]);
    }
}