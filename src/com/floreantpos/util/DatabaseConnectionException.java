package com.floreantpos.util;

public class DatabaseConnectionException extends Exception {

	public DatabaseConnectionException() {
	}

	public DatabaseConnectionException(String message) {
		super(message);
	}

	public DatabaseConnectionException(Throwable cause) {
		super(cause);
	}

	public DatabaseConnectionException(String message, Throwable cause) {
		super(message, cause);
	}

}
