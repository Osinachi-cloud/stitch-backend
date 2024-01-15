package com.stitch.model.dto;

import com.stitch.model.enums.OrderStatus;
import com.stitch.model.enums.PaymentMode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Currency;

@Data
//@Builder(toBuilder = true)
@NoArgsConstructor
public class ProductOrderDto {

    private OrderStatus status;
    private String referenceNumber;
    private String transactionId;
    private String message;
    private String clientSecret;
    private String paymentId;


    private String productCategoryName;
    private String vendor;
    private PaymentMode paymentMode;
    private BigDecimal amount;
    private String customerId;
    private String customerName;
    private String email;
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
    //    private OrderFrequency frequency;
//    private ScheduleStatus scheduleStatus;
    private String orderId;

//
}
