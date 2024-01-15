package com.stitch.model.dto;

import com.stitch.model.enums.PaymentMode;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Currency;

@Data
public class ProductOrderRequestDto {

    private String category;
    private String vendor;
    private PaymentMode paymentMode;
    private BigDecimal amount;
    private String customerId;
    private String customerName;
    private String email;
    private String cardId;
    private String txRef;
    private boolean saveCard;
    private String referenceNumber;
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

}
