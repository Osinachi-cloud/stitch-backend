package com.stitch.psp.model;

import com.stitch.currency.model.enums.Currency;
import com.stitch.psp.model.enums.PaymentProvider;
import lombok.Data;

@Data
public class PaymentVerificationResponse {

    private VerificationStatus status;
    private String transactionRefId;
    private String last4digits;
    private String first6digits;
    private String expiry;
    private String country;
    private String type;
    private String token;
    private Currency currency;
    private String cardFingerprint;
    private PaymentProvider paymentProvider;
}
