package com.stitch.payment.model.enums;

import lombok.Getter;

@Getter
public enum CardType {
    VISA("Visa"),
    MASTERCARD("Mastercard"),
    VERVE("Verve");

    private final String description;

    CardType(String description) {
        this.description = description;
    }

    public static CardType fromName(String name){
        for (CardType cardType: CardType.values()) {
            if(cardType.getDescription().equalsIgnoreCase(name)){
                return cardType;
            }
        }
        throw new IllegalArgumentException("Card type not found");
    }
}
