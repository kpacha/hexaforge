package com.hexaforge.util;

public class MemcacheKeyNotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	MemcacheKeyNotFoundException() {
		super("Key not found!");
	}

	MemcacheKeyNotFoundException(String msg) {
		super(msg);
	}

	MemcacheKeyNotFoundException(Exception e) {
		super(e);
	}
}