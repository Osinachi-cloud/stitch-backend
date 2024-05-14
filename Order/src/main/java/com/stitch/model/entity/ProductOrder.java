package com.stitch.model.entity;

import com.stitch.commons.model.entity.BaseEntity;
import com.stitch.currency.model.enums.Currency;
import com.stitch.model.enums.OrderStatus;
import com.stitch.model.enums.PaymentMode;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "product_order")
public class ProductOrder extends BaseEntity {


    @Column(name = "order_id", unique = true, nullable = false)
    private String orderId;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(name = "product_category_name")
    private String productCategoryName;

    @Column(name = "vendor_id")
    private String vendorId;

    @Column(name = "product_package")
    private String productPackage;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_mode")
    private PaymentMode paymentMode;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private Currency currency;

    @Column(name = "amount")
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "narration")
    private String narration;

    @Column(name = "description")
    private String description;

}
