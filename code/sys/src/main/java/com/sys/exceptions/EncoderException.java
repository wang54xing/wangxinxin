package com.sys.exceptions;

public class EncoderException  extends Exception{

	private static final long serialVersionUID = 1L;

	EncoderException() {
		super();
	}

	EncoderException(String message) {
		super(message);
	}

	public EncoderException(Throwable cause) {
		super(cause);
	}

	EncoderException(String message, Throwable cause) {
		super(message, cause);
	}

}
