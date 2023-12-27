package com.stitch.wallet.exception;

public class InvalidWalletException extends WalletException {
    public InvalidWalletException() {
    }

    public InvalidWalletException(String message) {
        super(message);
    }

    public InvalidWalletException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidWalletException(Throwable cause) {
        super(cause);
    }

    public InvalidWalletException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
