package com.stitch.wallet.model.enums;

public enum WalletStatus {

    A("Active"),
    I ("Inactive"),
    PND("Post-No-Debit"),
    S("Suspended"),
    C("Closed");

    private String description;
    WalletStatus(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
