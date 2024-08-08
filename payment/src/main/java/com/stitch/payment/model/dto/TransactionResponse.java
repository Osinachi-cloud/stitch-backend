package com.stitch.payment.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionResponse {

    private Long id;
    private String transactionId;
    private String orderId;
    private String customerId;
    private BigDecimal amount;
    private String currency;
    private String paymentMode;
    private String srcCurrency;
    private String destCurrency;
    private BigDecimal fee;
    private String productCategory;
    private String narration;
    private String description;
    private String status;
    private Long dateCreated;
    private Long lastUpdated;

}
