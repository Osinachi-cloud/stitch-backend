package com.stitch.wallet.model.enums;

public enum TransactionType {


    C("Credit"),
    D("Debit");


    private String name;
    TransactionType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
