package com.stitch.payment.model.entity;

import com.stitch.commons.model.entity.BaseEntity;
import com.stitch.currency.model.enums.Currency;
import com.stitch.payment.model.enums.PaymentMode;
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
public class Transaction extends BaseEntity {

    @Column(name = "transaction_id", unique = true, nullable = false)
    private String transactionId;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "wallet_transaction_id")
    private String walletTransactionId;

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "wallet_id")
    private String walletId;

    @Column(name = "payment_card_id")
    private String paymentCardId;

    @Column(name = "card_transaction_id")
    private String cardTransactionId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(name = "payment_mode")
    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode;

    @Column(name = "src_amount")
    private BigDecimal srcAmount;

    @Column(name = "src_currency")
    @Enumerated(EnumType.STRING)
    private Currency srcCurrency;

    @Column(name = "dest_amount")
    private BigDecimal destAmount;

    @Column(name = "dest_currency")
    @Enumerated(EnumType.STRING)
    private Currency destCurrency;

    @Column(name = "exchange_rate")
    private BigDecimal exchangeRate;

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
