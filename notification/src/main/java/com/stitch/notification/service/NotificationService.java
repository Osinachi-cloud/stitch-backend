package com.stitch.notification.service;

import com.stitch.notification.model.Email;
import com.stitch.notification.model.dto.ReceiptBuilder;
import org.springframework.scheduling.annotation.Async;

public interface NotificationService {


    @Async
    void sendEmail(Email email);

    void sendEmail(String htmlBody, String[] recipients, String subject, String from);

    @Async
    void emailVerification(String[] emailAddress, String  code);

    @Async
    void welcome(String[] emailAddress, String name);

    @Async
    void resetPasswordRequest(String[] emailAddress, String code, String name);

    @Async
    void resetPasswordSuccess(String[] emailAddress, String name);

    @Async
    void resetPin(String[] emailAddress, String code, String name);

    @Async
    void sendReceipt(ReceiptBuilder builder);

    @Async
    void walletFunding(String[] emailAddress, String amount, String balance, String firstName);
}
