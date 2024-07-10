package com.stitch.wallet.model.dto;

import com.stitch.wallet.model.entity.WalletTransactionRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WalletTransactionRequestDto {

    private String transactionId;
    private String customerId;
    private String walletId;
    private String currency;
    private BigDecimal amount;
    private String transactionType;
    private String status;
    private String paymentId;
    private String cardId;
    private String clientSecret;
    private String description;
    private String remarks;


    public static WalletTransactionRequestDto from(WalletTransactionRequest transactionRequest){
        return WalletTransactionRequestDto.builder()
                .transactionId(transactionRequest.getTransactionId())
                .currency(transactionRequest.getCurrency().name())
                .amount(transactionRequest.getAmount())
                .transactionType(transactionRequest.getTransactionType().getName())
                .status(transactionRequest.getStatus().getDescription())
                .walletId(transactionRequest.getWalletId())
                .customerId(transactionRequest.getUserId())
                .description(transactionRequest.getDescription())
                .build();
    }

}
