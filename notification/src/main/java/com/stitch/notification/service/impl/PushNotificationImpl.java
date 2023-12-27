package com.stitch.notification.service.impl;

import com.stitch.notification.model.PushNotification;
import com.stitch.notification.model.dto.ExpoPushRequest;
import com.stitch.notification.model.dto.PushRequest;
import com.stitch.notification.repository.PushNotificationRepository;
import com.stitch.notification.service.PushNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PushNotificationImpl implements PushNotificationService {

    private final PushNotificationRepository pushNotificationRepository;

    private final RestTemplate restTemplate;

    private String expoUrl;

    public PushNotificationImpl(PushNotificationRepository pushNotificationRepository,
                                RestTemplate restTemplate

//                               , @Value("${expo.push.url}")String expoUrl
    ) {
        this.pushNotificationRepository = pushNotificationRepository;
        this.restTemplate = restTemplate;
//        this.expoUrl = expoUrl;
    }


    public void saveCustomerToken(String customerId, String token){
        Optional<PushNotification> pushNotificationOptional = pushNotificationRepository.findByToken(token);
        PushNotification pushNotification = null;

        if(pushNotificationOptional.isPresent()){
            pushNotification = pushNotificationOptional.get();
            pushNotification.setCustomerId(customerId);
            pushNotificationRepository.saveAndFlush(pushNotification);
            return;
        }
        pushNotification = new PushNotification();
        pushNotification.setCustomerId(customerId);
        pushNotification.setToken(token);
        pushNotificationRepository.saveAndFlush(pushNotification);
    }

    @Async
    public void sendPushNotification(PushRequest pushRequest){
        List<PushNotification> pushNotificationList =  pushNotificationRepository.findAllByCustomerId(pushRequest.getCustomerId());
        if(pushNotificationList != null && !pushNotificationList.isEmpty()){
            pushNotificationList.forEach(item -> {
                try{
                    sendToExpo(ExpoPushRequest.builder()
                        .to(item.getToken())
                        .title(pushRequest.getSubject())
                        .body(pushRequest.getContent())
                        .sound("default")
                        .data(ExpoPushRequest.Data.builder()
                            .someData("default")
                            .build())
                        .build());
                }catch (Exception ex){
                    log.error("Failed to send push for {}", item.getToken(), ex);
                }
            });
        }
    }

    private void sendToExpo(ExpoPushRequest request) {

        log.info("sending push notification for token: {}", request.getTo());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Accept-encoding", "gzip, deflate");

        ResponseEntity<Object> response = restTemplate.exchange(expoUrl, HttpMethod.POST,
            new HttpEntity<>(request, headers), Object.class);

        log.debug("Response = {}", response);
    }
}
