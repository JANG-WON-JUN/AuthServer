package com.my.authserver.common.web.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

public class RoleHierarchyNotFound extends AuthServerException {

	public RoleHierarchyNotFound(String message) {
		super(message);
	}

	public RoleHierarchyNotFound(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public HttpStatus getStatusCode() {
		return BAD_REQUEST;
	}
}
