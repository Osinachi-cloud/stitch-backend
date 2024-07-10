package com.stitch.payment.model.entity;

import com.stitch.commons.model.entity.BaseEntity;
import com.stitch.currency.model.enums.Currency;
import com.stitch.payment.model.enums.PaymentMode;
import com.stitch.payment.model.enums.ReversalStatus;
import lombok.*;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payment_reversal_request")
public class PaymentReversalRequest extends BaseEntity {

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "wallet_id")
    private String walletId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(name = "payment_mode")
    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ReversalStatus status;

    @Column(name = "comments", columnDefinition = "text")
    private String comments;

}
