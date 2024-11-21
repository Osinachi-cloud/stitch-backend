package com.stitch.model.entity;

import com.stitch.commons.model.entity.BaseEntity;
import com.stitch.currency.model.enums.Currency;
import com.stitch.model.enums.OrderStatus;
import com.stitch.model.enums.PaymentMode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@Entity
@Table(name = "product_order")
public class ProductOrder extends BaseEntity {

    @Column(name = "order_id", unique = true, nullable = false)
    private String orderId;

    @Column(name = "transaction_id", unique = true, nullable = false)
    private String transactionId;

    @Column(name = "email_address", nullable = false)
    private String emailAddress;

    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(name = "product_category_name")
    private String productCategoryName;

    @Column(name = "vendor_email_address")
    private String vendorEmailAddress;

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

    @Column(name = "quantity")
    private BigDecimal quantity;

    @Column(name = "body_measurement_id")
    private Long bodyMeasurementId;

    @Column(name = "sleeve_type")
    private String sleeveType;

    @Column(name = "color")
    private String color;

    @Column(name = "body_measurement_tag")
    private String bodyMeasurementTag;

}
