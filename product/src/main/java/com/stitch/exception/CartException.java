package com.stitch.exception;

public class CartException  extends ProductException{
    public CartException(String message, int code) {
        super(message, code);
    }
}
