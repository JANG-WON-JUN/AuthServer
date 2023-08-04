package com.my.authserver.common.web.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

public class PasswordNotValid extends AuthServerException {

	public PasswordNotValid(String message) {
		super(message);
	}

	public PasswordNotValid(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public HttpStatus getStatusCode() {
		return BAD_REQUEST;
	}
}
