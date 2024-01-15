package com.stitch.commons.enums;

import org.springframework.http.HttpStatus;

public enum ResponseStatus {

    SUCCESS(0, "Successful",HttpStatus.OK),
    INVALID_PHONE_NUMBER(2, "The phone number is invalid", HttpStatus.BAD_REQUEST),
    PHONE_NUMBER_NOT_FOUND(3, "The phone number was not found", HttpStatus.NOT_FOUND),
    EMAIL_ADDRESS_NOT_FOUND(4, "The email address was not found", HttpStatus.NOT_FOUND),
    INVALID_VERIFICATION_CODE(5, "The verification code is invalid", HttpStatus.BAD_REQUEST),
    EXPIRED_VERIFICATION_CODE(6, "The verification code has expired", HttpStatus.BAD_REQUEST),
    PHONE_NUMBER_EXISTS(10, "A user with that phone number already exists", HttpStatus.BAD_REQUEST),
    EMAIL_ADDRESS_EXISTS(11, "A user with that email address already exists", HttpStatus.BAD_REQUEST),

    USERNAME_EXISTS(26, "A user with that username already exists", HttpStatus.BAD_REQUEST),

    INVALID_USER_ID(12, "The user Id is invalid", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(13,"The user could not be found", HttpStatus.NOT_FOUND),
    INVALID_EMAIL_ADDRESS(14,"The email address is invalid", HttpStatus.BAD_REQUEST),
    PASSWORD_MISMATCH(15, "Passwords do not match",HttpStatus.BAD_REQUEST),
    OLD_PASSWORD_INCORRECT(16, "The old password is incorrect",HttpStatus.BAD_REQUEST),
    PASSWORD_EMPTY(17, "Empty password(s) provided",HttpStatus.BAD_REQUEST),
    EMAIL_ADDRESS_UNVERIFIED(18, "Email address has not been verified", HttpStatus.BAD_REQUEST),
    PASSWORD_RESET_CODE_UNVERIFIED(19, "Password reset code sent to email has not been verified", HttpStatus.BAD_REQUEST),

    INVALID_RESET_CODE(20, "The reset code is invalid", HttpStatus.BAD_REQUEST),

    EXPIRED_RESET_CODE(21, "The reset code has expired", HttpStatus.BAD_REQUEST),

    INVALID_PIN_UPDATE(22,"Pin cannot be updated", HttpStatus.BAD_REQUEST),

    PIN_FORMAT_ERROR(23,"Pin must be 4 digit", HttpStatus.BAD_REQUEST),

    WRONG_PIN(24,"Wrong Pin!!", HttpStatus.BAD_REQUEST),

    RESET_PIN_FLAG(25,"exceeded", HttpStatus.BAD_REQUEST),

    INSUFFICIENT_WALLET_BALANCE(40, "Wallet balance is insufficient for debit", HttpStatus.BAD_REQUEST),

    EMPTY_FIELD_VALUES(90, "Empty field values provided", HttpStatus.BAD_REQUEST),
    FIELD_VALIDATION_ERROR(95, "The request field(s) failed validations", HttpStatus.BAD_REQUEST),
    REQUEST_BODY_INVALID(96, "The request body is malformed and could not be parsed", HttpStatus.UNPROCESSABLE_ENTITY),
    UNAUTHORIZED_ACCESS(97, "Access denied, authentication required", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED(98, "Token is expired", HttpStatus.FORBIDDEN),
    TOKEN_INVALID(99, "Invalid token", HttpStatus.BAD_REQUEST),
    PROCESSING_ERROR(500, "Error processing request", HttpStatus.INTERNAL_SERVER_ERROR);


    private final int code;
    private final String message;
    private final HttpStatus httpStatus;


    ResponseStatus(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;

    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

