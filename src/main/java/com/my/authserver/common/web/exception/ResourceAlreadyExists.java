package com.my.authserver.common.web.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

public class ResourceAlreadyExists extends AuthServerException {

	public ResourceAlreadyExists(String message) {
		super(message);
	}

	public ResourceAlreadyExists(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public HttpStatus getStatusCode() {
		return BAD_REQUEST;
	}
}
