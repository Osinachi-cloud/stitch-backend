package com.stitch.model.dto;

import com.stitch.commons.model.entity.BaseEntity;
import com.stitch.currency.model.enums.Currency;
import com.stitch.model.enums.OrderStatus;
import com.stitch.model.enums.PaymentMode;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;




@Data

public class ProductOrderRequest {

    private String orderId;

    private String customerId;

    private String productId;

    private String productCategoryName;

    private String vendorId;

    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private BigDecimal amount;

//    @Enumerated(EnumType.STRING)
    private String status;

    private String narration;


}
