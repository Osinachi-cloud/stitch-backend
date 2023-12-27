package com.stitch.wallet.exception;


import com.stitch.commons.exception.StitchException;

public class WalletNotFoundException extends StitchException {
    public WalletNotFoundException() {
        super("Wallet not found");
    }

    public WalletNotFoundException(String message) {
        super("Wallet not found: "+message);
    }

    public WalletNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public WalletNotFoundException(Throwable cause) {
        super(cause);
    }

    public WalletNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
