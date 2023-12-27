package com.stitch.commons.exception;

public class StitchException extends RuntimeException{

    public StitchException() {
        super("Error processing request at the moment.");
    }

    public StitchException(String message) {
        super(message);
    }

    public StitchException(String message, Throwable cause) {
        super(message, cause);
    }

    public StitchException(Throwable cause) {
        super(cause);
    }

    public StitchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
