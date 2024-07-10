//package com.stitch.vendor.vtpass.impl;
//
//import com.stitch.exception.StitchApiException;
//import com.stitch.exception.RetryableBillerApiException;
//import com.stitch.model.dtos.*;
//import com.stitch.vendor.vtpass.ElectricityVendor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.retry.annotation.Backoff;
//import org.springframework.retry.annotation.Retryable;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.HttpServerErrorException;
//import org.springframework.web.client.ResourceAccessException;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Objects;
//
//@Service
//@Slf4j
//public class VTPassElectricityVendor implements ElectricityVendor {
//
//    private final RestTemplate restTemplate;
//
//    @Value("${billers.vtpass.verify-meter-url}")
//    private String VERIFY_METER_URL;
//    @Value("${billers.vtpass.api-key}")
//    private String API_KEY;
//
//    @Value("${billers.vtpass.secret-key}")
//    private String SK_KEY;
//
//    @Value("${billers.vtpass.base-url}")
//    private String BASE_URL_VTPASS;
//
//    public VTPassElectricityVendor(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
//
//
//    @Override
//    @Retryable(value = RetryableBillerApiException.class, maxAttemptsExpression = "${billers.retry.maxAttempts}", backoff = @Backoff(delayExpression = "${billers.retry.maxDelay}"))
//    public VTPassElectricityPrePaidResponseDto vendPrepaidElectricity(VTPassElectricityPaymentRequest electricityPayload) {
//
//        log.debug("Vending prepaid electricity with payload: {}", electricityPayload);
//
//        try {
//            String url = BASE_URL_VTPASS + "/pay";
//            HttpEntity<VTPassElectricityPaymentRequest> request = new HttpEntity<>(electricityPayload, getHeaders());
//
//            log.debug("Making API call to VTPass to purchase prepaid electricity at URL: {}", url);
//            ResponseEntity<VTPassElectricityPrePaidResponseDto> response = restTemplate.exchange(
//                    url, HttpMethod.POST, request, VTPassElectricityPrePaidResponseDto.class);
//
//            log.debug("Prepaid Electricity Response {}", response);
//
//            return Objects.requireNonNull(response.getBody());
//        } catch (HttpServerErrorException e) {
//            log.error("VTPass server error encountered: " + e.getMessage());
//            if (e.getStatusCode().equals(HttpStatus.SERVICE_UNAVAILABLE)) {
//                log.warn("VTPass service is unavailable, a retry to be initiated");
//                throw new RetryableBillerApiException("Service unavailable. Retry required", e);
//            }
//            throw new StitchApiException("Service error making payment for electricity", e);
//        } catch (ResourceAccessException e) {
//            log.error("Server is unavailable or network issues: " + e.getMessage());
//            throw new RetryableBillerApiException("Service is unavailable at the moment", e);
//        } catch (Exception e) {
//            throw new StitchApiException("Error making payment for electricity", e);
//        }
//
//    }
//
//    @Override
//    @Retryable(value = RetryableBillerApiException.class, maxAttemptsExpression = "${billers.retry.maxAttempts}", backoff = @Backoff(delayExpression = "${billers.retry.maxDelay}"))
//    public VTPassElectricityPostPaidResponseDto vendPostpaidElectricity(VTPassElectricityPaymentRequest electricityPayload) {
//
//        log.debug("Vending postpaid electricity with payload: {}", electricityPayload);
//
//        try {
//            String url = BASE_URL_VTPASS + "/pay";
//            HttpEntity<VTPassElectricityPaymentRequest> request = new HttpEntity<>(electricityPayload, getHeaders());
//
//            log.debug("Making API call to VTPass to purchase postpaid electricity at URL: {}", url);
//            ResponseEntity<VTPassElectricityPostPaidResponseDto> response = restTemplate.exchange(
//                    url, HttpMethod.POST, request, VTPassElectricityPostPaidResponseDto.class);
//
//            log.debug("Postpaid Electricity Response {}", response);
//
//            return Objects.requireNonNull(response.getBody());
//        } catch (HttpServerErrorException e) {
//            log.error("VTPass server error encountered: " + e.getMessage());
//            if (e.getStatusCode().equals(HttpStatus.SERVICE_UNAVAILABLE)) {
//                log.warn("VTPass service is unavailable, a retry to be initiated");
//                throw new RetryableBillerApiException("Service unavailable. Retry required", e);
//            }
//            throw new StitchApiException("Service error making payment for electricity", e);
//        } catch (ResourceAccessException e) {
//            log.error("Server is unavailable or network issues: " + e.getMessage());
//            throw new RetryableBillerApiException("Service is unavailable at the moment", e);
//        } catch (Exception e) {
//            throw new StitchApiException("Error making payment for electricity", e);
//        }
//
//    }
//
//
//    @Override
//    public MeterValidationResponse validateMeterNumber(VTPassElectricityMeterValidation meterValidationRequest) {
//
//        try {
//            HttpEntity<VTPassElectricityMeterValidation> request = new HttpEntity<>(meterValidationRequest, getHeaders());
//            ResponseEntity<VTPassMeterValidationDto> response = restTemplate.exchange(
//                    VERIFY_METER_URL, HttpMethod.POST, request, VTPassMeterValidationDto.class);
//
//            log.debug("Meter validation response {}", response.getBody());
//
//            return MeterValidationResponse.builder()
//                    .name(Objects.requireNonNull(response.getBody()).getContent().getCustomerName())
//                    .number(Objects.requireNonNull(response.getBody()).getContent().getMeterNumber())
//                    .address(Objects.requireNonNull(response.getBody()).getContent().getAddress())
//                    .build();
//        } catch (HttpServerErrorException e) {
//            log.error("Service error validating meter number", e);
//            throw new StitchApiException("Service error validating meter number", e);
//        } catch (Exception e) {
//            log.error("Error validating meter number", e);
//            throw new StitchApiException("Error validating meter number", e);
//        }
//    }
//
//    private HttpHeaders getHeaders() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("api-key", API_KEY);
//        headers.set("secret-key", SK_KEY);
//        return headers;
//    }
//}
