package com.stitch.model.dto;

import com.stitch.currency.model.enums.Currency;
import com.stitch.model.enums.PaymentMode;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data

public class ProductOrderRequest {

    private String orderId;

    private String transactionId;

    private BigDecimal quantity;

    private BigDecimal amount;

    private String emailAddress;

    private String productId;

    private String productCategoryName;

    private String vendorEmailAddress;

    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private String status;

    private String narration;

    private String sleeveType;

    private String color;

    private String bodyMeasurementTag;

}
