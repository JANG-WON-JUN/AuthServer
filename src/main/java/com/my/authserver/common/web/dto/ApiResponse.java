package com.my.authserver.common.web.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiResponse<T> {

    private int code;
    private HttpStatus status;
    private String message;
    private T data;

    @Builder
    private ApiResponse(T data) {
        this.data = data;
    }

    private ApiResponse(HttpStatus status, String message, T data) {
        this.code = status.value();
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> of(HttpStatus httpStatus, String message, T data) {
        return new ApiResponse<>(httpStatus, message, data);
    }

    public static <T> ApiResponse<T> of(HttpStatus httpStatus, T data) {
        return of(httpStatus, httpStatus.name(), data);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return of(HttpStatus.OK, data);
    }

    public static ApiResponse<String> ok() {
        return of(HttpStatus.OK, "OK");
    }

}
