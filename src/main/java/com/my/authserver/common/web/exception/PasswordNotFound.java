package com.my.authserver.common.web.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

public class PasswordNotFound extends AuthServerException {

	public PasswordNotFound(String message) {
		super(message);
	}

	public PasswordNotFound(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public HttpStatus getStatusCode() {
		return BAD_REQUEST;
	}
}
