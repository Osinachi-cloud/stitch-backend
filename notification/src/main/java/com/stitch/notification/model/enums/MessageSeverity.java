package com.stitch.notification.model.enums;

import lombok.Getter;

@Getter
public enum MessageSeverity {
    H("High"),
    M("Medium"),
    L("Low");

    private final String  description;

    MessageSeverity(String description) {
        this.description = description;
    }
}
