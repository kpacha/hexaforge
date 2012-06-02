package com.hexaforge.command;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import com.google.appengine.api.users.User;
import com.hexaforge.core.interfaces.GameInterface;
import com.hexaforge.entity.GameEntity;

public class CheckTurnsCommand extends AbstractCommand {

    public CheckTurnsCommand(EntityManager entityManager) {
	super(entityManager);
    }

    public List<String> getGameIdsToCheck() {
	long now = (new Date()).getTime();
	List<GameEntity> games = gameDAO.getGamesToCheck(now);
	List<String> gameIds = new ArrayList<String>();
	for (GameEntity game : games) {
	    if (now - game.getNextCheck() >= 0)
		gameIds.add(game.getId());
	}
	return gameIds;
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected GameInterface doExecute(Map parameters, User user)
	    throws Exception {
	throw new Exception("Method not implemented!");
    }
}
