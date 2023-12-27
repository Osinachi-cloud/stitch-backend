package com.stitch.exception;

import com.stitch.commons.exception.StitchException;

public class BillingException extends StitchException {

    private String requestId;
    public BillingException() {
    }

    public BillingException(String message) {
        super(message);
    }


    public BillingException(String message, Throwable cause) {
        super(message, cause);
    }

    public BillingException(String requestId, String message, Throwable cause) {
        super(message, cause);
        this.requestId = requestId;
    }

    public BillingException(Throwable cause) {
        super(cause);
    }

    public BillingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public String getRequestId() {
        return requestId;
    }
}
