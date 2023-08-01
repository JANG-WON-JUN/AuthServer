package com.my.authserver.common.web.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

public class RoleAlreadyExists extends AuthServerException {

	public RoleAlreadyExists(String message) {
		super(message);
	}

	public RoleAlreadyExists(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public HttpStatus getStatusCode() {
		return BAD_REQUEST;
	}
}
