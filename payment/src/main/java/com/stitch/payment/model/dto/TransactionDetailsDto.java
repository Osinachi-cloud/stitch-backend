package com.stitch.payment.model.dto;

import com.stitch.payment.model.enums.TransactionType;
import lombok.Data;

import java.time.Instant;

@Data
public class TransactionDetailsDto {

    private String orderId;

    private String customerId;

    private TransactionType transactionType;

    private String transactionId;

    private Instant createdOn;

    private String phoneNumber;

    private String sourceAmount;

    private String sourceCurrency;

    private String amount;

    private String currency;

    private String productCategoryName;

    private String productProvider;

    private String productPackage;

    private String billersCode;

    private String variationCode;

    private String customerName;

    private String customerAddress;

    private String token;

    private String units;

}
