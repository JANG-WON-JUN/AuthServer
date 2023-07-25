package com.my.authserver.web.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class BoardNotFound extends AuthServerException {

    public BoardNotFound(String message) {
        super(message);
    }

    public BoardNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatusCode() {
        return BAD_REQUEST;
    }
}
