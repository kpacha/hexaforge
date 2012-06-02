package com.hexaforge.command.exception;

public class JoinException extends Exception {

    private static final long serialVersionUID = 1L;

    public JoinException() {
	super("Unable to join the game");
    }
}
