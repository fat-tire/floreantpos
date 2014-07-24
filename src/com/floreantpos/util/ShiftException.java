package com.floreantpos.util;

public class ShiftException extends RuntimeException {

	public ShiftException() {
		super();
	}

	public ShiftException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ShiftException(String message, Throwable cause) {
		super(message, cause);
	}

	public ShiftException(String message) {
		super(message);
	}

	public ShiftException(Throwable cause) {
		super(cause);
	}

}
