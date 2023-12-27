package com.stitch.payment.model.dto;

import com.stitch.currency.model.enums.Currency;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PaymentRequest {

    private BigDecimal amount;
    private Currency srcCurrency;
    private Currency destCurrency;
    private String customerId;
    private String emailAddress;
    private String fullName;
    private String paymentMethodId;
    private boolean savePaymentCard;


}
