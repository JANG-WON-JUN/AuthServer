package com.my.authserver.common.web.exception.dto;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import com.my.authserver.common.web.exception.AuthServerException;

public class ResourceNotFound extends AuthServerException {

	public ResourceNotFound(String message) {
		super(message);
	}

	public ResourceNotFound(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public HttpStatus getStatusCode() {
		return BAD_REQUEST;
	}
}
