package com.stitch.wallet.model.dto;

import com.stitch.wallet.model.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionRequest {

    private String customerId;
    private boolean enablePush;
    private String transactionId;
    private String cardId;
    private String currency;
    private BigDecimal amount;
    private String txRef;
    private String walletId;
    private String transactionType;
    private String description;
    private TransactionStatus status;
    private String pin;
    private String paymentProvider;
    private boolean saveCard;
    private String firstName;
    private String emailAddress;
}
