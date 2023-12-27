package com.stitch.notification.service;

public interface MailerService {

    public void sendEmail(String htmlBody, String[] recipients, String subject, String from);
}
