package com.stitch.psp.model.entity;

import com.stitch.commons.model.entity.BaseEntity;
import com.stitch.currency.model.enums.Currency;
import com.stitch.psp.model.enums.PaymentProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "payment_verification")
public class PaymentVerification extends BaseEntity {


    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    private PaymentProvider provider;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private Currency currency;

    @Column(name = "status")
    private String status;

    @Column(name = "request", columnDefinition = "text")
    private String request;

    @Column(name = "response", columnDefinition = "text")
    private String response;
}
