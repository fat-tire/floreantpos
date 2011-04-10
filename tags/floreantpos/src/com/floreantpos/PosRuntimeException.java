package com.floreantpos;

public class PosRuntimeException extends RuntimeException {

	public PosRuntimeException() {
		super();
	}

	public PosRuntimeException(String message) {
		super(message);
	}

	public PosRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public PosRuntimeException(Throwable cause) {
		super(cause);
	}

}
