package com.stitch.notification.service;

import com.stitch.notification.model.dto.PushRequest;

public interface PushNotificationService {

    void saveCustomerToken(String customerId, String token);

    void sendPushNotification(PushRequest pushRequest);
}
