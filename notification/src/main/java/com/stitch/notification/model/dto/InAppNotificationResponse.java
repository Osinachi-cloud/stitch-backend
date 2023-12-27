package com.stitch.notification.model.dto;

import com.stitch.notification.model.enums.MessageSeverity;
import lombok.Data;

@Data
public class InAppNotificationResponse {

    private String notificationId;

    private String customerId;

    private String subject;

    private String content;

    private MessageSeverity severity;

    private String dateTime;

    private boolean read;
}
