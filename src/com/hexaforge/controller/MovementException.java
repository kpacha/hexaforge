package com.hexaforge.controller;

public class MovementException extends Exception {

    private static final long serialVersionUID = 1L;

    public MovementException() {
	super("Not a valid movement");
    }

}
