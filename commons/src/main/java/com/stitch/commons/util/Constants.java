package com.stitch.commons.util;

import org.springframework.http.HttpStatus;

public final class Constants {

    private Constants() {}

    public static final String FAILED = "FAILED";

    public static HttpStatus status(int code){
        return (switch (code) {
            case 200 -> HttpStatus.OK;
            case 201 -> HttpStatus.CREATED;
            case 202 -> HttpStatus.ACCEPTED;
            case 400 -> HttpStatus.BAD_REQUEST;
            case 401 -> HttpStatus.UNAUTHORIZED;
            case 405 -> HttpStatus.CONFLICT;
            case 406 -> HttpStatus.NOT_ACCEPTABLE;
            case 417 -> HttpStatus.EXPECTATION_FAILED;
            case 503, 504  -> HttpStatus.SERVICE_UNAVAILABLE;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        });
    }
}
