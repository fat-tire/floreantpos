package com.floreantpos.model.util;

public class IllegalModelStateException extends Exception {

	public IllegalModelStateException() {
		super();
	}

	public IllegalModelStateException(String message) {
		super(message);
	}

	public IllegalModelStateException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalModelStateException(Throwable cause) {
		super(cause);
	}

}
