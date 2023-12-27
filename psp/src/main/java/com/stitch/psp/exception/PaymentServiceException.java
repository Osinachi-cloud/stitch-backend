package com.stitch.psp.exception;

import com.stitch.commons.exception.StitchException;

public class PaymentServiceException extends StitchException {

    public PaymentServiceException() {
    }

    public PaymentServiceException(String message) {
        super(message);
    }

    public PaymentServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentServiceException(Throwable cause) {
        super(cause);
    }

    public PaymentServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
