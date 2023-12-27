package com.stitch.notification.model.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InAppNotificationStatsResponse {
    private long unread;
    private long read;
    private long total;
}
