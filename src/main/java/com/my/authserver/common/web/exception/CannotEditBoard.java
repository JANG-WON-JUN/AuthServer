package com.my.authserver.common.web.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class CannotEditBoard extends AuthServerException {

	public CannotEditBoard(String message) {
		super(message);
	}

	public CannotEditBoard(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public HttpStatus getStatusCode() {
		return BAD_REQUEST;
	}
}
