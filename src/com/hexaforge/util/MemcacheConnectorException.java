package com.hexaforge.util;

@SuppressWarnings("serial")
public class MemcacheConnectorException extends Exception {
	MemcacheConnectorException(String msg) {
		super(msg);
	}

	MemcacheConnectorException(Exception e) {
		super(e);
	}
}
