package com.stitch.wallet.model.enums;

public enum TransactionRequestStatus {

    A("Accepted"),
    R("Rejected"),
    P("Pending"),
    S("Successful"),
    F("Failed"),
    C("Cancelled");


    private String description;
    TransactionRequestStatus(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
