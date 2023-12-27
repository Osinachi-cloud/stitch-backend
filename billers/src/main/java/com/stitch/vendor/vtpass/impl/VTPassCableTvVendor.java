package com.stitch.vendor.vtpass.impl;


import com.stitch.exception.StitchApiException;
import com.stitch.exception.RetryableBillerApiException;
import com.stitch.vendor.vtpass.CableTvVendor;
import com.stitch.model.dtos.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
@Slf4j
public class VTPassCableTvVendor implements CableTvVendor {

    private final RestTemplate restTemplate;

    @Value("${billers.vtpass.api-key}")
    private String API_KEY;

    @Value("${billers.vtpass.secret-key}")
    private String SK_KEY;

    @Value("${billers.vtpass.pub-key}")
    private String PUB_KEY;

    @Value("${billers.vtpass.cable-tv-package-url}")
    private String BASE_URL_CABLE_PACKAGE;

    @Value("${billers.vtpass.base-url}")
    private String BASE_URL_CABLE;



    public VTPassCableTvVendor(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Override
    public VTPassCablePackageDto getCableTvPackage(String serviceID) {

        try {
            String url = StringUtils.replace(BASE_URL_CABLE_PACKAGE, "cabletype", serviceID);

            HttpHeaders headers = getHeaders();

            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity<VTPassCablePackageDto> response = restTemplate.exchange(
                    url, HttpMethod.GET, request, VTPassCablePackageDto.class);

            log.info("Response body {}", response);
            return response.getBody();

        } catch (HttpServerErrorException e) {
            log.error("Service error getting cable TV packages", e);
            throw new StitchApiException("Error getting cable TV packages", e);
        } catch (Exception e) {
            log.error("Error getting cable TV packages", e);
            throw new StitchApiException("Error getting cable TV packages", e);
        }
    }

    @Override
    public VTPassCableInfoResponse cableInfoEnquiry(String serviceID, String smartCard) {

        try {
            String url = String.format("%s%s", BASE_URL_CABLE, "/merchant-verify");
            VTPassCableInfoRequest infoRequest = VTPassCableInfoRequest.builder()
                    .serviceID(serviceID)
                    .billersCode(smartCard)
                    .build();

            HttpHeaders headers = getHeaders();

            HttpEntity<VTPassCableInfoRequest> request = new HttpEntity<>(infoRequest, headers);
            ResponseEntity<VTPassCablePackageDto> response = restTemplate.exchange(
                    url, HttpMethod.POST, request, VTPassCablePackageDto.class);

            log.info("response body {}", response.getBody());

            return VTPassCableInfoResponse.builder()
                    .name(Objects.requireNonNull(response.getBody()).getContent().getCustomer_Name())
                    .number(Objects.requireNonNull(response.getBody()).getContent().getCustomer_Number())
                    .build();
        } catch (HttpServerErrorException e) {
            log.error("Service error verifying cable information", e);
            throw new StitchApiException("Error verifying cable information", e);
        } catch (Exception e) {
            log.error("Error verifying cable information", e);
            throw new StitchApiException("Error verifying cable information", e);
        }
    }

    @Override
    @Retryable(value = RetryableBillerApiException.class, maxAttemptsExpression = "${billers.retry.maxAttempts}", backoff = @Backoff(delayExpression = "${billers.retry.maxDelay}"))
    public VTPassCableTvPaymentResponseDto vendCablePlan(VTPassCableTvPaymentRequest vtPassCableTvPaymentRequest) {

        log.debug("Vending cable TV with payload: {}", vtPassCableTvPaymentRequest);

        try {
            String url = BASE_URL_CABLE + "/pay";
            HttpHeaders headers = getHeaders();

            HttpEntity<VTPassCableTvPaymentRequest> request = new HttpEntity<>(vtPassCableTvPaymentRequest, headers);

            log.debug("Making API call to VTPass to purchase cable TV plan at URL: {}", url);

            ResponseEntity<VTPassCableTvPaymentResponseDto> response = restTemplate.postForEntity(url, request, VTPassCableTvPaymentResponseDto.class);
            log.debug("Cable TV response: {}", response);

            return Objects.requireNonNull(response.getBody());

        } catch (HttpServerErrorException e) {
            log.error("VTPass server error encountered: " + e.getMessage());
            if (e.getStatusCode().equals(HttpStatus.SERVICE_UNAVAILABLE)) {
                log.warn("VTPass service is unavailable, a retry to be initiated");
                throw new RetryableBillerApiException("Service unavailable. Retry required", e);
            }
            throw new StitchApiException("Service error making payment for cable TV", e);
        } catch (ResourceAccessException e) {
            log.error("Server is unavailable or network issues: " + e.getMessage());
            throw new RetryableBillerApiException("Service is unavailable at the moment", e);
        } catch (Exception e) {
            throw new StitchApiException("Error making payment for cable TV", e);
        }
    }

    @Override
    public VTPassMultichoiceDTO validateCableId(String serviceID) {
        try {
            String url = StringUtils.replace(BASE_URL_CABLE_PACKAGE, "cabletype", serviceID);
            HttpHeaders headers = new HttpHeaders();
            setHeaders(headers);
            ResponseEntity<VTPassMultichoiceDTO> response = restTemplate.getForEntity(url, VTPassMultichoiceDTO.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new StitchApiException("Error validating cable TV ID", e);
        } catch (Exception e) {
            log.error("Error validating cable ID", e);
            throw new StitchApiException("Error validating cable TV ID", e);
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
