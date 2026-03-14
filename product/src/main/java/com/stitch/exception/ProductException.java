package com.stitch.exception;

import com.stitch.commons.exception.StitchException;

public class ProductException extends StitchException {
    final int code;

    public ProductException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
