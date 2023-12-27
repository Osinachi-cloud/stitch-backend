package com.stitch.notification.model.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ExpoPushRequest {

    private String to;
    private String sound;
    private String title;
    private String body;
    private Data data;

    @lombok.Data
    @Builder
    public static class Data{
        private String someData;
    }
}
