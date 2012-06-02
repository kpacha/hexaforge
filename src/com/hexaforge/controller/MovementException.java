package com.hexaforge.controller;

public class MovementException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	MovementException(){
		super("Not a valid movement");
	}

}
