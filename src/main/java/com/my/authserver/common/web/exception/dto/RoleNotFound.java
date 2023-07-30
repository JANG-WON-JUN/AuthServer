package com.my.authserver.common.web.exception.dto;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import com.my.authserver.common.web.exception.AuthServerException;

public class RoleNotFound extends AuthServerException {

	public RoleNotFound(String message) {
		super(message);
	}

	public RoleNotFound(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public HttpStatus getStatusCode() {
		return BAD_REQUEST;
	}
}
