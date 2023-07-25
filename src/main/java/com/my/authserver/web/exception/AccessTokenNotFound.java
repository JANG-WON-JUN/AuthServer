package com.my.authserver.web.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class AccessTokenNotFound extends AuthServerException {

    public AccessTokenNotFound() {
    }

    public AccessTokenNotFound(String message) {
        super(message);
    }

    public AccessTokenNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatusCode() {
        return UNAUTHORIZED;
    }
}
