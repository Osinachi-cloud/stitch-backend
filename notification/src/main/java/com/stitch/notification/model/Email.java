package com.stitch.notification.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Email {

    private String senderEmail;
    private String senderName;
    private String[] recipientEmails;
    private String subject;
    private String htmlBody;
}
