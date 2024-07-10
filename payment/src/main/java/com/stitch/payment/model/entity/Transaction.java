package com.stitch.payment.model.entity;

import com.stitch.commons.model.entity.BaseEntity;
import com.stitch.currency.model.enums.Currency;
import com.stitch.payment.model.enums.PaymentMode;
import com.stitch.payment.model.enums.Status;
import com.stitch.payment.model.enums.TransactionStatus;
import com.stitch.payment.model.enums.TransactionType;
import lombok.*;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transaction")
@ToString
public class Transaction extends BaseEntity {


//    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_id")
//    private String user;

    @Column(name = "reference")
    private String reference;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "gateway_response")
    private String gatewayResponse;

    @Column(name = "paid_at")
    private String paidAt;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "channel")
    private String channel;

    @Column(name = "currency")
    private String currency;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "transaction_id", unique = true, nullable = false)
    private String transactionId;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "wallet_transaction_id")
    private String walletTransactionId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "wallet_id")
    private String walletId;

    @Column(name = "payment_card_id")
    private String paymentCardId;

    @Column(name = "card_transaction_id")
    private String cardTransactionId;

    @Column(name = "payment_mode")
    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode;

    @Column(name = "fee")
    private BigDecimal fee;

    @Column(name = "product_category")
    private String productCategory;

    @Column(name = "narration")
    private String narration;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(name = "transaction_type")
    @Enumerated(EnumType.ORDINAL)
    private TransactionType transactionType;
}
