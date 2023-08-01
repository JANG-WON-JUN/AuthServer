package com.my.authserver.common.web.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

public class RoleHierarchyNotValid extends AuthServerException {

	public RoleHierarchyNotValid(String message) {
		super(message);
	}

	public RoleHierarchyNotValid(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public HttpStatus getStatusCode() {
		return BAD_REQUEST;
	}
}
