package com.stitch.notification.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class EmailConfigProperties {

//    @Value("${sender.email:Billanted<dev.billanted@exquisapps.com>}")
    private String senderEmail;

//    @Value("${sender.name:Billanted}")
    private String senderName;

//    @Value("${email.dir}")
    public String directory ;
}
