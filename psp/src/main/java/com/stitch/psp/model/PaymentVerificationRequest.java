package com.stitch.psp.model;

import com.stitch.psp.model.enums.PaymentProvider;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentVerificationRequest {

    private String transactionRefId;
    private String currency;
    private BigDecimal amount;
    private PaymentProvider paymentProvider;
}
