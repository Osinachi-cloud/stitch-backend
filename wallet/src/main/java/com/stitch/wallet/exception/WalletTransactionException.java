package com.stitch.wallet.exception;


public class WalletTransactionException extends WalletException {
    public WalletTransactionException() {
        super("Wallet transaction not found");
    }

    public WalletTransactionException(String message) {
        super(message);
    }

    public WalletTransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public WalletTransactionException(Throwable cause) {
        super(cause);
    }

    public WalletTransactionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
