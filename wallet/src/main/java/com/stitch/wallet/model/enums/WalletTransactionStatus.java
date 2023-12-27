package com.stitch.wallet.model.enums;

public enum WalletTransactionStatus {

    A("Accepted"),
    R("Rejected"),
    P("Processing"),
    S("Successful"),
    F("Failed"),
    C("Cancelled");

    private String name;
    WalletTransactionStatus(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
