package com.stitch.exception;


public class RetryableBillerApiException extends StitchApiException {
    public RetryableBillerApiException() {
    }

    public RetryableBillerApiException(String message) {
        super(message);
    }

    public RetryableBillerApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public RetryableBillerApiException(Throwable cause) {
        super(cause);
    }

    public RetryableBillerApiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
