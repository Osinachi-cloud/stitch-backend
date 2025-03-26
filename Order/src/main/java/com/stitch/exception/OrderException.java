package com.stitch.exception;

import com.stitch.commons.exception.StitchException;
import lombok.Getter;

@Getter
public class OrderException extends StitchException {
    final int code;
    public OrderException(String message, int code) {
        super(message);
        this.code = code;
    }
}
