package com.my.authserver.web.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class RefreshTokenNotFound extends AuthServerException {

    public RefreshTokenNotFound(String message) {
        super(message);
    }

    public RefreshTokenNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatusCode() {
        return UNAUTHORIZED;
    }
}
