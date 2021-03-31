package com.eventsweb.challenge.services.exceptions;

public class VacanciesUnavailableException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public VacanciesUnavailableException(String msg) {
		super(msg);
	}
}
