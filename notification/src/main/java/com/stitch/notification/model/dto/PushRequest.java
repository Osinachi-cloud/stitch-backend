package com.stitch.notification.model.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PushRequest {

    private String customerId;

    private String subject;

    private String content;
}
