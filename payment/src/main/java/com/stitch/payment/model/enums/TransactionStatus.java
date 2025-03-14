package com.stitch.payment.model.enums;

public enum TransactionStatus {
    PROCESSING("PROCESSING"),
    COMPLETED("COMPLETED"),
    FAILED("FAILED"),
    REJECTED("REJECTED");


    final String description;

    TransactionStatus(String description) {
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
}
