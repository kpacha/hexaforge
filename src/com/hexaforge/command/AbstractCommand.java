package com.hexaforge.command;

import java.util.Map;

import javax.persistence.EntityManager;

import com.google.appengine.api.users.User;
import com.hexaforge.core.decorator.JsonDecorator;
import com.hexaforge.core.interfaces.GameInterface;
import com.hexaforge.entity.GameDAO;
import com.hexaforge.entity.GameEntity;

abstract public class AbstractCommand {

    protected JsonDecorator game2Json;
    protected GameEntity gameEntity;
    protected GameDAO gameDAO;

    public AbstractCommand(EntityManager entityManager) {
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

    protected static int parameter2Int(String paramName,
	    Map<String, String[]> parameters) {
	return Integer.parseInt(parameters.get(paramName)[0]);
    }

    public String execute(Map<String, String[]> parameters, User user)
	    throws Exception {
	setGameEntity(parameters.get("pid")[0]);
	GameInterface game = doExecute(parameters, user);
	return game2Json.serializeGame(game);
    }

    abstract protected GameInterface doExecute(
	    Map<String, String[]> parameters, User user) throws Exception;
}