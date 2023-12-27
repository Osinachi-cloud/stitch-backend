package com.stitch.wallet.model.enums;

public enum BaseWalletStatus {

    A("Active"),
    I ("Inactive"),
    C("Closed");

    private String description;
    BaseWalletStatus(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
