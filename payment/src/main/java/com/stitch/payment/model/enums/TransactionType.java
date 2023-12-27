package com.stitch.payment.model.enums;

/**
 * The TransactionType sequence order is very important
 * and SHOULD NOT be changed as the ordinal value is persisted.
 * Any additional enum value should be added below.
 */
public enum TransactionType {
    PAY_BILL ("Bill Payment"),
    FUND_WALLET("Fund Wallet"),

    SCHEDULED_BILL ("Scheduled Bill Payment");

    private final String description;


    TransactionType(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
