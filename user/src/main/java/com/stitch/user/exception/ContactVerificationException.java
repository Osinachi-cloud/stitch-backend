package com.stitch.user.exception;


import com.stitch.commons.enums.ResponseStatus;

public class ContactVerificationException extends UserException {


    public ContactVerificationException(String message) {
        super(message);
    }

    public ContactVerificationException(ResponseStatus status) {
        super(status);
    }

    public ContactVerificationException(ResponseStatus status, Object details) {
        super(status, details);
    }

    public ContactVerificationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContactVerificationException(ResponseStatus status, Throwable cause) {
        super(status, cause);
    }

}
