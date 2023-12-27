package com.stitch.payment.model.enums;

public enum TierActionType {
    F("Fund"),
    W("Withdraw");

    private final String description;

    TierActionType(String description) {
        this.description = description;
    }
}
