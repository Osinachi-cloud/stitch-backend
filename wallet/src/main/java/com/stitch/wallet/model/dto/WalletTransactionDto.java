package com.stitch.wallet.model.dto;

import com.stitch.wallet.model.enums.TransactionStatus;
import com.stitch.wallet.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WalletTransactionDto {


    private String walletId;
    private String transactionId;
    private String currency;
    private BigDecimal amount;
    private TransactionType transactionType;
    private TransactionStatus status;
    private String description;
    private String orderId;
    private String paymentTransactionId;
    private BigDecimal exchangeRate;
    private BigDecimal balance;


}
