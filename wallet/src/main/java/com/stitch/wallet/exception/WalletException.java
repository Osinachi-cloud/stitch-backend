package com.stitch.wallet.exception;

import com.stitch.commons.exception.StitchException;

public class WalletException extends StitchException {
    public WalletException() {
    }

    public WalletException(String message) {
        super(message);
    }

    public WalletException(String message, Throwable cause) {
        super(message, cause);
    }

    public WalletException(Throwable cause) {
        super(cause);
    }

    public WalletException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
