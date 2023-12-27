package com.stitch.wallet.model.enums;

public enum TransactionStatus {

    S("Successful"),
    F("Failed"),
    P("Pending"),
    C("Cancelled");


    private String description;
    TransactionStatus(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
