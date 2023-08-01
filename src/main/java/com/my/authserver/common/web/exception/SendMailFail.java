package com.my.authserver.common.web.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

public class SendMailFail extends AuthServerException {

	public SendMailFail(String message) {
		super(message);
	}

	public SendMailFail(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public HttpStatus getStatusCode() {
		return INTERNAL_SERVER_ERROR;
	}
}
