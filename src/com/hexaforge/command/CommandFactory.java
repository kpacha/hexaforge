package com.hexaforge.command;

import javax.persistence.EntityManager;

public class CommandFactory {

    public static AbstractCommand getCommand(String commandType,
	    EntityManager entityManager) throws Exception {
	AbstractCommand command;
	if (commandType.equalsIgnoreCase("view"))
	    command = new ViewCommand(entityManager);
	else if (commandType.equalsIgnoreCase("new"))
	    command = new NewGameCommand(entityManager);
	else if (commandType.equalsIgnoreCase("join"))
	    command = new JoinCommand(entityManager);
	else if (commandType.equalsIgnoreCase("start"))
	    command = new StartCommand(entityManager);
	else if (commandType.equalsIgnoreCase("move"))
	    command = new MoveCommand(entityManager);
	else if (commandType.equalsIgnoreCase("updateTurns"))
	    command = new UpdateTurnsCommand(entityManager);
	else if (commandType.equalsIgnoreCase("checkTurns"))
	    command = new CheckTurnsCommand(entityManager);
	else
	    throw new Exception("Unknown action [" + commandType + "]");
	return command;
    }
}
