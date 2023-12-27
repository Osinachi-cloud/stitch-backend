package com.stitch.notification.model.dto;

import com.stitch.notification.model.enums.MessageSeverity;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class InAppNotificationRequest {

    private String customerId;

    private String subject;

    private String content;

    private MessageSeverity severity;
}
