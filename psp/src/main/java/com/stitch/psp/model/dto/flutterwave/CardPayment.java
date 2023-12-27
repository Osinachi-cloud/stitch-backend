package com.stitch.psp.model.dto.flutterwave;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stitch.currency.model.enums.Currency;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class CardPayment {
    private String customerId;
    private String email;
    private String token;
    private String country;
    private Currency srcCurrency;
    private Currency destCurrency;
    private BigDecimal amount;

    @JsonProperty("tx_ref")
    private String transactionReference;
    private String narration;
}
