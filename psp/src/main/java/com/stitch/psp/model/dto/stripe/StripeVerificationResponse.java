package com.stitch.psp.model.dto.stripe;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StripeVerificationResponse {

    private String paymentId;
    private String status;
    private String currency;
    private Long amount;
    private String paymentMethodId;
    private String last4digits;
    private String country;
    private String cardType;
    private Long cardExpMonth;
    private Long cardExpYear;
    private String cardFingerprint;
    private String rawResponse;

}
