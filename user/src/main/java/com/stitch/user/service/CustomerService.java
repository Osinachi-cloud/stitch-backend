package com.stitch.user.service;

import com.stitch.commons.model.dto.Response;
//import com.stitch.notification.model.dto.InAppNotificationResponse;
//import com.stitch.notification.model.dto.InAppNotificationStatsResponse;
import com.stitch.user.model.dto.CustomerDto;
import com.stitch.user.model.dto.CustomerRequest;
import com.stitch.user.model.dto.CustomerUpdateRequest;
import com.stitch.user.model.dto.PasswordResetRequest;

import java.util.List;

public interface CustomerService {


    CustomerDto createCustomer(CustomerRequest customerRequest);



    CustomerDto updateCustomer(CustomerUpdateRequest customerRequest, String emailAddress);

    Response updateCustomerProfileImage(String profileImage, String emailAddress);

    CustomerDto getCustomer(String customerId);

    CustomerDto getCustomerByEmail(String emailAddress);

    void updateLastLogin(CustomerDto user);

    void updateLoginAttempts(String emailAddress);

    Response requestPasswordReset(String emailAddress);

    Response resetPassword(PasswordResetRequest passwordResetRequest);

    Response validatePasswordResetCode(PasswordResetRequest passwordResetRequest);

    Response createPin(String customerId, String pin);

    Response checkPin(String customerId, String pin);

    Response resetPinInitiateEmail(String customerId, String phoneNumber);

    Response verifyResetPinCode(String customerId, String code);

    Response resetPin(String customerId, String pin);

    Response allowSaveCard(String customerId, Boolean saveCard);

//    Response enablePushNotification(String customerId, Boolean enablePush);
//
//    Response addPushNotificationToken(String customerId, String pushToken);

//    List<InAppNotificationResponse> customerInAppNotifications(String customerId, int page, int size);
//
//    Response updateReadInAppNotification(String customerId, String notificationId);
//
//    InAppNotificationStatsResponse inAppNotificationMessagesStats(String customerId);
}
