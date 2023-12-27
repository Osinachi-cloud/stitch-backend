package com.stitch.vendor.vtpass.impl;

import com.stitch.exception.StitchApiException;
import com.stitch.exception.RetryableBillerApiException;
import com.stitch.model.dtos.VTPassAirtimePaymentResponse;
import com.stitch.model.dtos.VTPassAirtimeRequest;
import com.stitch.vendor.vtpass.AirtimeVendor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;


@Service
@Slf4j
public class VTPassAirtimeVendor implements AirtimeVendor {
    private final RestTemplate restTemplate;

    @Value("${billers.vtpass.base-url}")
    private String BASE_URL_VTPASS;
    @Value("${billers.vtpass.api-key}")
    private String API_KEY;
    @Value("${billers.vtpass.secret-key}")
    private String SK_KEY;


    public VTPassAirtimeVendor(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Override
    @Retryable(value = RetryableBillerApiException.class, maxAttemptsExpression = "${billers.retry.maxAttempts}", backoff = @Backoff(delayExpression = "${billers.retry.maxDelay}"))
    public VTPassAirtimePaymentResponse vendAirTime(VTPassAirtimeRequest airtimePayload) {

        log.debug("Vending airtime with payload: {}", airtimePayload);

        try {
            String url = BASE_URL_VTPASS + "/pay";
            HttpHeaders headers = getHeaders();
            HttpEntity<?> entity = new HttpEntity<>(airtimePayload, headers);

            log.debug("Making API call to VTPass to purchase airtime at URL: {}", url);

            ResponseEntity<VTPassAirtimePaymentResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, VTPassAirtimePaymentResponse.class);
            log.debug("Airtime response: {}", response);

            return Objects.requireNonNull(response.getBody());
        } catch (HttpServerErrorException e) {
            log.error("VTPass server error encountered: " + e.getMessage());
            if (e.getStatusCode().equals(HttpStatus.SERVICE_UNAVAILABLE)) {
                log.warn("VTPass service is unavailable, a retry to be initiated");
                throw new RetryableBillerApiException("Service unavailable. Retry required", e);
            }
            throw new StitchApiException("Service error making payment for airtime", e);
        } catch (ResourceAccessException e) {
            log.error("Server is unavailable or network issues: " + e.getMessage());
            throw new RetryableBillerApiException("Service is unavailable at the moment", e);
        } catch (Exception e) {
            throw new StitchApiException("Error making payment for airtime", e);
        }
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", API_KEY);
        headers.set("secret-key", SK_KEY);
        return headers;
    }
}
