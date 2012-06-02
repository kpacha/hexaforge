package com.hexaforge.core;

public class ExceptionMessage {
    private String message;
    @SuppressWarnings("unused")
    private static final boolean SUCCESS = false;

    public ExceptionMessage(String message) {
	this.setMessage(message);
    }

    /**
     * @return the message
     */
    public String getMessage() {
	return message;
    }

    /**
     * @param message
     *            the message to set
     */
    private void setMessage(String message) {
	this.message = message;
    }
}
