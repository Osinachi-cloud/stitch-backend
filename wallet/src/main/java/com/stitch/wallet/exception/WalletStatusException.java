package com.stitch.wallet.exception;


public class WalletStatusException extends WalletException {
    public WalletStatusException() {
    }

    public WalletStatusException(String message) {
        super(message);
    }

    public WalletStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public WalletStatusException(Throwable cause) {
        super(cause);
    }

    public WalletStatusException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
