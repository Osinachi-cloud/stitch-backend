//package com.stitch.notification.service.impl;
//
//import com.stitch.notification.config.EmailConfigProperties;
//import com.stitch.notification.model.Email;
//import com.stitch.notification.model.dto.ReceiptBuilder;
//import com.stitch.notification.service.NotificationService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//import org.thymeleaf.context.Context;
//import org.thymeleaf.spring5.SpringTemplateEngine;
//
//@Slf4j
//@Service
//public class NotificationServiceImpl implements NotificationService {
//    private final AwsMailerServiceImpl mailerService;
//
//    private final EmailConfigProperties configProperties;
//
//    private final SpringTemplateEngine springTemplateEngine;
//    public NotificationServiceImpl(AwsMailerServiceImpl mailerService,
//                                   EmailConfigProperties configProperties,
//                                   SpringTemplateEngine springTemplateEngine) {
//        this.mailerService = mailerService;
//        this.configProperties = configProperties;
//        this.springTemplateEngine = springTemplateEngine;
//    }
//
//
//    @Async
//    @Override
//    public void sendEmail(Email email) {
//        log.debug("Sending email: {}", email);
//        mailerService.sendEmail(email.getHtmlBody(), email.getRecipientEmails(), email.getSubject(), email.getSenderEmail());
//    }
//
//    @Async
//    @Override
//    public void sendEmail(String htmlBody, String[] recipients, String subject, String from) {
//        mailerService.sendEmail(htmlBody, recipients, subject, from);
//    }
//
//    @Async
//    @Override
//    public void emailVerification(String[] emailAddress, String code) {
//        Context context  = new Context();
//        context.setVariable("code", code);
//        String html = springTemplateEngine.process(configProperties.getDirectory() + "EmailVerification", context);
//
//        Email email = Email.builder()
//            .subject("Email Verification")
//            .senderEmail(configProperties.getSenderEmail())
//            .recipientEmails(emailAddress)
//            .senderName(configProperties.getSenderName())
//            .htmlBody(html)
//            .build();
//
//        sendEmail(email);
//    }
//
//    @Async
//    @Override
//    public void welcome(String[] emailAddress, String name) {
//        Context context  = new Context();
//        context.setVariable("name", name);
//        String html = springTemplateEngine.process(configProperties.getDirectory() + "WelcomeEmail", context);
//
//        Email email = Email.builder()
//            .subject("Welcome to Billanted")
//            .senderEmail(configProperties.getSenderEmail())
//            .recipientEmails(emailAddress)
//            .senderName(configProperties.getSenderName())
//            .htmlBody(html)
//            .build();
//
//        sendEmail(email);
//    }
//
//    @Async
//    @Override
//    public void resetPasswordRequest(String[] emailAddress, String code, String name) {
//        Context context  = new Context();
//        context.setVariable("code", code);
//        context.setVariable("name", name);
//        String html = springTemplateEngine.process(configProperties.getDirectory() + "PasswordReset", context);
//
//        Email email = Email.builder()
//            .subject("Request for Password Reset")
//            .senderEmail(configProperties.getSenderEmail())
//            .recipientEmails(emailAddress)
//            .senderName(configProperties.getSenderName())
//            .htmlBody(html)
//            .build();
//
//        sendEmail(email);
//    }
//
//    @Async
//    @Override
//    public void resetPasswordSuccess(String[] emailAddress, String name) {
//        Context context  = new Context();
//        context.setVariable("name", name);
//        String html = springTemplateEngine.process(configProperties.getDirectory() + "PasswordResetSuccessful", context);
//
//        Email email = Email.builder()
//            .subject("Password Reset")
//            .senderEmail(configProperties.getSenderEmail())
//            .recipientEmails(emailAddress)
//            .senderName(configProperties.getSenderName())
//            .htmlBody(html)
//            .build();
//
//        sendEmail(email);
//    }
//
//    @Async
//    @Override
//    public void resetPin(String[] emailAddress, String code, String name) {
//        Context context  = new Context();
//        context.setVariable("code", code);
//        context.setVariable("name", name);
//        String html = springTemplateEngine.process(configProperties.getDirectory() + "ResetTransactionPin", context);
//
//        Email email = Email.builder()
//            .subject("Reset Pin")
//            .senderEmail(configProperties.getSenderEmail())
//            .recipientEmails(emailAddress)
//            .senderName(configProperties.getSenderName())
//            .htmlBody(html)
//            .build();
//
//        sendEmail(email);
//    }
//
//    @Async
//    @Override
//    public void sendReceipt(ReceiptBuilder builder) {
//
//        Context context = new Context();
//        context.setVariable("created_on", builder.getDate());
//        context.setVariable("amount", builder.getAmount());
//        context.setVariable("first_name", builder.getFirstName());
////        context.setVariable("src_amount", String.format("%s%s", detailsDto.getSourceCurrency(), detailsDto.getSourceAmount()));
//        context.setVariable("transaction_id", builder.getTransactionId());
//        context.setVariable("meter_token", builder.getElectricityToken());
//        context.setVariable("token_units", builder.getUnits());
//        context.setVariable("bill_type", builder.getBillType().substring(0, 1).toUpperCase() + builder.getBillType().substring(1));
//
//        String html = springTemplateEngine.process(configProperties.getDirectory() + "PaymentReceipt", context);
//
//        Email email = Email.builder()
//            .subject("Payment Receipt")
//            .senderEmail(configProperties.getSenderEmail())
//            .recipientEmails(new String[]{builder.getEmail()})
//            .senderName(configProperties.getSenderName())
//            .htmlBody(html)
//            .build();
//
//        sendEmail(email);
//    }
//
//    @Override
//    public void walletFunding(String[] emailAddress, String amount, String balance, String firstName) {
//        Context context = new Context();
//        context.setVariable("first_name", firstName);
//        context.setVariable("amount", amount);
//        context.setVariable("balance", balance);
//
//        String html = springTemplateEngine.process(configProperties.getDirectory() + "WalletFundedSuccessfully", context);
//
//        Email email = Email.builder()
//            .subject("Wallet Funded Successfully")
//            .senderEmail(configProperties.getSenderEmail())
//            .recipientEmails(emailAddress)
//            .senderName(configProperties.getSenderName())
//            .htmlBody(html)
//            .build();
//
//        sendEmail(email);
//    }
//}
