package com.my.authserver.web.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class CommentNotFound extends AuthServerException {

    public CommentNotFound(String message) {
        super(message);
    }

    public CommentNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatusCode() {
        return BAD_REQUEST;
    }
}
