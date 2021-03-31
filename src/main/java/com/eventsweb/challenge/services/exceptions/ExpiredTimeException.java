package com.eventsweb.challenge.services.exceptions;

public class ExpiredTimeException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ExpiredTimeException(String msg) {
		super(msg);
	}
}
