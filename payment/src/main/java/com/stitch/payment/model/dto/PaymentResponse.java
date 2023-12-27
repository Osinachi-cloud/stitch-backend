package com.stitch.payment.model.dto;


import com.stitch.currency.model.enums.Currency;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentResponse {
    private String transactionRefId;
    private BigDecimal exchangeRate;
    private Currency currency;
    private String clientSecret;
}
