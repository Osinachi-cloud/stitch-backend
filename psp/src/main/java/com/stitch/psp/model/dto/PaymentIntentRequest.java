package com.stitch.psp.model.dto;

import com.stitch.currency.model.enums.Currency;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PaymentIntentRequest {

    private BigDecimal amount;
    private Currency currency;
    private String customerId;
    private String emailAddress;
    private String fullName;
    private String paymentMethodId;
    private boolean savePaymentCard;
}
