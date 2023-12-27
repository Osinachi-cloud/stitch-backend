package com.stitch.wallet.model.dto;

import com.stitch.currency.model.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
public class BaseWalletCreditRequest {

    private String customerId;
    private String walletId;
    private BigDecimal amount;
    private Currency currency;
    private String customerWalletTransactionId;
}
