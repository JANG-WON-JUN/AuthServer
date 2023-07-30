package com.my.authserver.common.web.exception.dto;

import com.my.authserver.common.web.exception.AuthServerException;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

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
