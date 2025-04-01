package com.stitch.payment.exception;

import com.stitch.commons.exception.StitchException;

public class PaymentException extends StitchException {

    final int code;

    public PaymentException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
