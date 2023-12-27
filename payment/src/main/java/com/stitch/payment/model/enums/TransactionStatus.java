package com.stitch.payment.model.enums;

public enum TransactionStatus {
    C("Completed"),
    P("Pending"),
    F("Failed");


    final String description;

    TransactionStatus(String description) {
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
}
