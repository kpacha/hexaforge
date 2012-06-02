package com.hexaforge.command.exception;

public class MovementException extends Exception {

    private static final long serialVersionUID = 1L;

    public MovementException() {
	super("Not a valid movement");
    }

}
