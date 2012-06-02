package com.hexaforge.command;

import java.util.Date;
import java.util.Map;

import javax.persistence.EntityManager;

import com.google.appengine.api.users.User;
import com.hexaforge.command.exception.UpdateTurnsException;
import com.hexaforge.core.interfaces.GameInterface;

public class UpdateTurnsCommand extends AbstractCommand {

    public UpdateTurnsCommand(EntityManager entityManager) {
	super(entityManager);
    }

    protected GameInterface doExecute(Map<String, String[]> parameters,
	    User user) throws Exception {
	GameInterface game = game2Json.deserializeGame(gameEntity.getGame());
	long now = (new Date()).getTime();
	if (game.getNextCheck() > now)
	    throw new UpdateTurnsException(
		    "Can not update turns before the nextCheck time! Wait "
			    + ((game.getNextCheck() - now) / 1000) + "sec.");
	if (!game.addTurns(game.getPreferences().getDeltaTurn()))
	    throw new UpdateTurnsException();
	game.setNextCheck(game.getNextCheck()
		+ game.getPreferences().getEtaTurn());
	gameDAO.persist(game, gameEntity);
	return game;
    }
}