package com.stitch.model.entity;


import com.stitch.commons.model.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bill_transaction")
public class BillTransaction extends BaseEntity {

    @Column(name = "request_id")
    private String requestId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "transaction_type")
    private String transactionType;

    @Column(name = "billers_code")
    private String billersCode;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "biller_amount")
    private BigDecimal billerAmount;

    @Column(name = "commission")
    private Double commission;

    @Column(name = "convinience_fee")
    private Double convenienceFee;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "vendor_name")
    private String vendorName;

    @Column(name = "service_id")
    private String serviceId;

    @Column(name = "variation_code")
    private String variationCode;

    @Column(name = "token")
    private String token;

    @Column(name = "units")
    private String units;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "status_code")
    private String statusCode;

    @Column(name = "response_description")
    private String responseDescription;

    @Column(name = "user_address")
    private String userAddress;

    @Column(name = "transaction_status")
    private String transactionStatus;


    @Column(name = "request_payload", columnDefinition = "text")
    private String requestPayload;

    @Column(name = "response_payload", columnDefinition = "text")
    private String responsePayload;


    @Column(name = "comments", columnDefinition = "text")
    private String comments;
}
