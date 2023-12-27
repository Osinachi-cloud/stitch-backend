package com.stitch.psp.model.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PaymentIntentResponse {

    private BigDecimal amount;
    private String currency;
    private String providerCustomerId;
    private String paymentId;
    private String clientSecret;
    private String ephemeralKeySecret;
    private String status;
}
