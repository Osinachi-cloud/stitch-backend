package com.stitch.payment.model.dto;

import com.stitch.payment.model.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@Entity
//@Table(name= "transaction")
public class TransactionDto {


    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private String user;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

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

//    @Column(name = "pricing_plan_type", nullable = false)
//    @Enumerated(EnumType.STRING)
//    private pricing plan type PlanType = PricingPlanType BASIC;

//    @CreationTimestamp
//    @Temporal(TemporalType.TIMESTAMP)
//    @Column(name = "created_on", updatable = false, nullable = false)
//    private String createdOn;
}