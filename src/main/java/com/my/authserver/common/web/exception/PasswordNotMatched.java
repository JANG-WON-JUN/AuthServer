package com.my.authserver.common.web.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

public class PasswordNotMatched extends AuthServerException {

	public PasswordNotMatched(String message) {
		super(message);
	}

	public PasswordNotMatched(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public HttpStatus getStatusCode() {
		return BAD_REQUEST;
	}
}
