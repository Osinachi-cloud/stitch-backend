package com.stitch.vendor.vtpass.impl;

import com.stitch.exception.StitchApiException;
import com.stitch.exception.RetryableBillerApiException;
import com.stitch.model.dtos.*;
import com.stitch.vendor.vtpass.DataVendor;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
public class VTPassDataVendor implements DataVendor {
    private final RestTemplate restTemplate;

    @Value("${billers.vtpass.api-key}")
    private String API_KEY;

    @Value("${billers.vtpass.secret-key}")
    private String SK_KEY;

    @Value("${billers.vtpass.dataplan-url}")
    private String BASE_URL_VTPASS_DATA_PLAN;

    @Value("${billers.vtpass.base-url}")
    private String BASE_URL_VTPASS;


    public VTPassDataVendor(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Override
    @Retryable(value = RetryableBillerApiException.class, maxAttemptsExpression = "${billers.retry.maxAttempts}", backoff = @Backoff(delayExpression = "${billers.retry.maxDelay}"))
    public VTPassDataPaymentResponseDto vendData(VTPassDataRequest dataPayload) {

        log.debug("Vending data with payload: {}", dataPayload);

        try {

            String url = BASE_URL_VTPASS + "/pay";
            HttpHeaders headers = getHeaders();
            HttpEntity<?> entity = new HttpEntity<>(dataPayload, headers);

            log.debug("Making API call to VTPass to purchase data at URL: {}", url);

            ResponseEntity<VTPassDataPaymentResponseDto> response = restTemplate.exchange(url, HttpMethod.POST, entity, VTPassDataPaymentResponseDto.class);
            log.debug("Data response: {}", response);

            return Objects.requireNonNull(response.getBody());
        } catch (HttpServerErrorException e) {
            log.error("VTPass server error encountered: " + e.getMessage());
            if (e.getStatusCode().equals(HttpStatus.SERVICE_UNAVAILABLE)) {
                log.warn("VTPass service is unavailable, a retry to be initiated");
                throw new RetryableBillerApiException("Service unavailable. Retry required", e);
            }
            throw new StitchApiException("Service error making payment for data", e);
        } catch (ResourceAccessException e) {
            log.error("Server is unavailable or network issues: " + e.getMessage());
            throw new RetryableBillerApiException("Service is unavailable at the moment", e);
        }  catch (Exception e) {
            throw new StitchApiException("Error making payment for data", e);
        }
    }

    @Override
    public DataPlanDto getDataPackage(String network) {

        log.debug("Getting data packages for {}", network);

        String url = StringUtils.replace(BASE_URL_VTPASS_DATA_PLAN, "network", network);

        try {
            HttpHeaders headers = new HttpHeaders();
            setHeaders(headers);
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            log.info(response.getBody());
            Gson gson = new Gson();
            DataPlanContent responseBody = gson.fromJson(response.getBody(), DataPlanContent.class);

            return DataPlanDto.builder()
                    .success(true)
                    .content(responseBody)
                    .build();
        } catch (HttpServerErrorException e) {
            log.error("Error getting data packages for {}", network, e);
            throw new StitchApiException("Service error getting data packages", e);
        } catch (Exception e) {
            log.error("Error getting data packages", e);
            throw new StitchApiException("Error getting data packages", e);
        }
    }


    @Override
    public VTPassDataEmailVerificationResponse verifySmileEmail(String serviceID, String emailAddress) {

        log.debug("Verifying Smile email for {}", emailAddress);

        try {
            String url = String.format("%s%s", BASE_URL_VTPASS, "/merchant-verify/smile/email");
            SmileEmailVerificationRequest verificationRequest = SmileEmailVerificationRequest.builder()
                    .serviceID(serviceID)
                    .billersCode(emailAddress)
                    .build();

            HttpHeaders headers = getHeaders();

            HttpEntity<SmileEmailVerificationRequest> request = new HttpEntity<>(verificationRequest, headers);
            ResponseEntity<SmileEmailVerificationResponse> response = restTemplate.exchange(
                    url, HttpMethod.POST, request, SmileEmailVerificationResponse.class);

            log.debug("Verification response {}", response.getBody());

            VTPassDataEmailVerificationResponse verificationResponse = new VTPassDataEmailVerificationResponse();
            verificationResponse.setCustomerName(Objects.requireNonNull(response.getBody()).getContent().getCustomerName());
            Objects.requireNonNull(response.getBody()).getContent().getAccountList().getAccounts().forEach(a -> {
                VTPassDataEmailVerificationResponse.Account account = new VTPassDataEmailVerificationResponse.Account();
                account.setAccountName(a.getFriendlyName());
                account.setAccountId(a.getAccountId().toString());
                verificationResponse.getAccounts().add(account);
            });
            return verificationResponse;
        } catch (HttpServerErrorException e) {
            log.error("Error verifying Smile email for {}", emailAddress, e);
            throw new StitchApiException("Service error verifying Smile email", e);
        } catch (Exception e) {
            log.error("Error verifying Smile email", e);
            throw new StitchApiException("Error verifying Smile email", e);
        }
    }


    private void setHeaders(HttpHeaders headers) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", API_KEY);
        headers.set("secret-key", SK_KEY);
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", API_KEY);
        headers.set("secret-key", SK_KEY);
        return headers;
    }
}
