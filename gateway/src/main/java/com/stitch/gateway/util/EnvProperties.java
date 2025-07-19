package com.stitch.gateway.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
public class EnvProperties {

    @Value("${security.token.access.secret-key}")
    private String accessTokenSecretKey;

    @Value("${security.token.access.expiry-length}")
    private long accessTokenExpiryInMilliseconds;

    @Value("${security.token.refresh.secret-key}")
    private String refreshTokenSecretKey;

    @Value("${security.token.refresh.expiry-length}")
    private long refreshTokenExpiryInMilliseconds;

    @Value("${cors.allowed-credential}")
    private boolean allowCORS;

    @Value("${cors.allowed-origins}")
    private List<String> allowedOrigins;

    @Value("${cors.allowed-methods}")
    private List<String> allowedMethods;

    @Value("${cors.allowed-headers}")
    private List<String> allowedHeaders;

    @Value("${paystack.secret-key}")
    private String paystackSecretKey;

    @Value("${paystack.initialize-payment-url}")
    private String initializePaymentUrl;

    @Value("${paystack.call-back-url}")
    private String callBackURL;

    @Value("${paystack.verification-url}")
    private String paystackVerificationUrl;

}
