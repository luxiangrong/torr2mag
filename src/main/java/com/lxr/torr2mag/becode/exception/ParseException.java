package com.lxr.torr2mag.becode.exception;

public class ParseException extends RuntimeException {

	private static final long serialVersionUID = 2877524836377161798L;

	public ParseException(String message) {
		super(message);
	}

	public ParseException(String message, Throwable e) {
		super(message, e);
	}
}
