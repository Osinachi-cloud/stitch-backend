package com.stitch.payment.model.dto;


import com.stitch.currency.model.enums.Currency;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
public class CardPaymentRequest {

    private String cardId;
    private BigDecimal amount;
    private String narration;
    private String customerId;
    private String email;
    private Currency srcCurrency;
    private Currency destCurrency;
    private String orderId;
}
