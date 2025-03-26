package com.stitch.gateway.exception;

import com.stitch.commons.exception.StitchException;
import lombok.Getter;

@Getter
public class ApiException extends StitchException {
    final int code;
    public ApiException(String message, int code) {
        super(message);
        this.code = code;
    }
}
