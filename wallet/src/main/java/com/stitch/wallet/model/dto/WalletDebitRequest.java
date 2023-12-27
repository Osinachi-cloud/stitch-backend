package com.stitch.wallet.model.dto;

import com.stitch.currency.model.enums.Currency;
import com.stitch.wallet.model.enums.TransactionStatus;
import com.stitch.wallet.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletDebitRequest {

    private String customerId;
    private String walletId;
    private BigDecimal amount;
    private Currency currency;
    private TransactionStatus transactionStatus;
    private TransactionType transactionType = TransactionType.D;
    private  String description;
    private String orderId;

}
