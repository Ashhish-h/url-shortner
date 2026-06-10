package com.url_shortner.url_shortner.exception;

import lombok.Getter;

@Getter
public enum ErrorCodes {

    NOT_FOUND(404, "NOT_FOUND"),
    BAD_REQUEST(400, "BAD_REQUEST"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR");

    private final Integer code;
    private final String message;

    ErrorCodes(Integer code, String message) {
        this.code = code;
        this.message = message;
    }



}
