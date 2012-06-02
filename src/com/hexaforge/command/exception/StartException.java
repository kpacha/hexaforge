package com.hexaforge.command.exception;

public class StartException extends Exception {

    private static final long serialVersionUID = 1L;

    public StartException() {
	super("Unable to start the game");
    }
}
