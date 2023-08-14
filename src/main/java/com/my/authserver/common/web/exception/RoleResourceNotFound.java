package com.my.authserver.common.web.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

public class RoleResourceNotFound extends AuthServerException {

	public RoleResourceNotFound(String message) {
		super(message);
	}

	public RoleResourceNotFound(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public HttpStatus getStatusCode() {
		return BAD_REQUEST;
	}
}
