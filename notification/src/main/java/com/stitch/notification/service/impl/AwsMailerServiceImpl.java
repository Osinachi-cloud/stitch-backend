package com.stitch.notification.service.impl;


import com.stitch.notification.service.MailerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AwsMailerServiceImpl  implements MailerService {

    @Override
    public void sendEmail(String htmlBody, String[] recipients, String subject, String from) {

        try{

        } catch (Exception ex){
            log.error("Failed to send mail due to: ", ex);
        }
    }



}
