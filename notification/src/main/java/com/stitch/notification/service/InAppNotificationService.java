package com.stitch.notification.service;

import com.stitch.notification.model.dto.InAppNotificationRequest;
import com.stitch.notification.model.dto.InAppNotificationResponse;
import com.stitch.notification.model.dto.InAppNotificationStatsResponse;

import java.util.List;

public interface InAppNotificationService {

    void saveInAppNotification(InAppNotificationRequest request);

    void updateReadInAppNotification(String notificationId, String customerId);

    void deleteInAppNotification(String notificationId, String customerId);

    List<InAppNotificationResponse> fetchAllCustomerNotification(String customerId, int page, int size);

    void saveBulkInAppNotification(List<InAppNotificationRequest> request);

    InAppNotificationStatsResponse customerInAppNotificationStats(String customerId);
}
