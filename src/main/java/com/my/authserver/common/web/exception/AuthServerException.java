package com.my.authserver.common.web.exception;

import lombok.NoArgsConstructor;

import org.springframework.http.HttpStatus;

@NoArgsConstructor
public abstract class AuthServerException extends RuntimeException {

	public abstract HttpStatus getStatusCode();

	public AuthServerException(String message) {
		super(message);
	}

	public AuthServerException(String message, Throwable cause) {
		super(message, cause);
	}
}
