package com.hexaforge.util;

public class MemcacheConnectorException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	MemcacheConnectorException(String msg) {
		super(msg);
	}

	MemcacheConnectorException(Exception e) {
		super(e);
	}
}