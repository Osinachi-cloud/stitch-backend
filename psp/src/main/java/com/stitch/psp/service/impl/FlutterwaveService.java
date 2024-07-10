//package com.stitch.psp.service.impl;
//
//import com.stitch.psp.model.dto.flutterwave.CardPayment;
//import com.stitch.psp.model.dto.flutterwave.FlutterwaveVerificationResponse;
//import com.stitch.psp.service.LocalPaymentProvider;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//@Slf4j
//@Service("flutterwave")
//public class FlutterwaveService implements LocalPaymentProvider {
//
//    private final RestTemplate restTemplate;
//
//    @Value("${psp.flutterwave.secret-key}")
//    private String SECRET_KEY;
//
//    @Value("${psp.flutterwave.base-url}")
//    private String BASE_URL;
//
//    @Value("${psp.flutterwave.verify-url}")
//    private String VERIFY_URL;
//
//    public FlutterwaveService(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
//
//
//    @Override
//    public Object verifyPayment(final String transactionId) {
//
//        log.info("Verifying Flutterwave payment transaction: {}", transactionId);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Content-Type", "application/json");
//        headers.set("Authorization", "Bearer "+SECRET_KEY);
//
//        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
//
//        String url = StringUtils.replace(VERIFY_URL, "<txRef>", transactionId);
//
//        ResponseEntity<FlutterwaveVerificationResponse> response = restTemplate.exchange(
//                url,
//                HttpMethod.GET,
//                requestEntity,
//                FlutterwaveVerificationResponse.class
//        );
//
//        FlutterwaveVerificationResponse verificationResponse = response.getBody();
//
//        log.debug("Response = {}", verificationResponse);
//
//        return verificationResponse;
//    }
//
//    @Override
//    public FlutterwaveVerificationResponse makeCardPayment(CardPayment cardPayment) {
//
//        log.debug("Making Flutterwave card payment: {}", cardPayment);
//
//        HttpEntity<Object> requestEntity = new HttpEntity<>(cardPayment, getFlutterwaveHeader());
//
//        String url = String.format("%s/%s", BASE_URL, "tokenized-charges");
//
//        ResponseEntity<FlutterwaveVerificationResponse> response = restTemplate.exchange(
//            url,
//            HttpMethod.POST,
//            requestEntity,
//            FlutterwaveVerificationResponse.class
//        );
//
//        FlutterwaveVerificationResponse verificationResponse = response.getBody();
//
//        log.debug("Response = {}", verificationResponse);
//
//        return verificationResponse;
//    }
//
//
//    private HttpHeaders getFlutterwaveHeader(){
//        HttpHeaders headers = new HttpHeaders();
//        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
//        headers.set(HttpHeaders.AUTHORIZATION, "Bearer "+SECRET_KEY);
//        return headers;
//    }
//}
