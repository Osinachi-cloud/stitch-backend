package com.stitch.wallet.model.dto;

import com.stitch.currency.model.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletDebitReversalRequest {

    private String walletTransactionId;
    private String customerId;
    private String walletId;
    private BigDecimal amount;
    private Currency currency;
    private String description;
}
