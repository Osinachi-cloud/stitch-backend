package com.stitch.gateway.exception;

import com.stitch.commons.exception.StitchException;

public class SecurityException extends StitchException {
    public SecurityException(String message) {
        super(message);
    }
}
