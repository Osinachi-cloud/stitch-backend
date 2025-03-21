package com.stitch.model.dto;

import com.stitch.currency.model.enums.Currency;
import com.stitch.model.enums.OrderStatus;
import com.stitch.model.enums.PaymentMode;
import com.stitch.user.model.dto.BodyMeasurementDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ProductOrderDto {

    private OrderStatus status;
    private String referenceNumber;
    private String transactionId;
    private String message;
    private String clientSecret;
    private String paymentId;
    private String productCategoryName;
    private String vendorEmailAddress;
    private PaymentMode paymentMode;
    private BigDecimal amount;
    private String customerId;
    private String customerName;
    private String emailAddress;
    private String cardId;
    private String txRef;
    private boolean saveCard;
    private Currency currency;
    private String narration;
    private String walletId;
    private String pin;
    private String psp;
    private boolean saveBeneficiary;
    private Integer number;
    private String startTime;
    private String dateCreated;
    private String orderId;
    private Long bodyMeasurementId;
    private BodyMeasurementDto bodyMeasurementDto;
    private BigDecimal quantity;
    private String bodyMeasurementTag;
    private ProductVariationDto productVariationDto;
}

