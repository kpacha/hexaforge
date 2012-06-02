package com.hexaforge.command.exception;

public class UpdateTurnsException extends Exception {

    private static final long serialVersionUID = 1L;

    public UpdateTurnsException(String string) {
	super(string);
    }

    public UpdateTurnsException() {
	super("Unable to update the turns");
    }
}
