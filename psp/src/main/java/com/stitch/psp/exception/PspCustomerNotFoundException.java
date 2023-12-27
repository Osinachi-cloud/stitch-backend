package com.stitch.psp.exception;

public class PspCustomerNotFoundException extends PaymentServiceException {

    public PspCustomerNotFoundException() {
    }

    public PspCustomerNotFoundException(String message) {
        super(message);
    }

    public PspCustomerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PspCustomerNotFoundException(Throwable cause) {
        super(cause);
    }

    public PspCustomerNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
