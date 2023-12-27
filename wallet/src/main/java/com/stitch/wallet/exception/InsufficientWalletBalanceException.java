package com.stitch.wallet.exception;


public class InsufficientWalletBalanceException extends WalletException {
    public InsufficientWalletBalanceException() {
    }

    public InsufficientWalletBalanceException(String message) {
        super(message);
    }

    public InsufficientWalletBalanceException(String message, Throwable cause) {
        super(message, cause);
    }

    public InsufficientWalletBalanceException(Throwable cause) {
        super(cause);
    }

    public InsufficientWalletBalanceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
