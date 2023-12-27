package com.stitch.exception;

import com.stitch.commons.exception.StitchException;

public class StitchApiException extends StitchException {
    public StitchApiException() {
    }

    public StitchApiException(String message) {
        super(message);
    }

    public StitchApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public StitchApiException(Throwable cause) {
        super(cause);
    }

    public StitchApiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
