package com.stitch.psp.model.entity;

import com.stitch.commons.model.entity.BaseEntity;
import com.stitch.psp.model.enums.PaymentProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "psp_customer_info")
public class PspCustomerInfo extends BaseEntity {

    @Column(name = "billanted_user_id", nullable = false)
    private String billantedUserId;

    @Column(name = "psp_user_id", nullable = false)
    private String pspUserId;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_provider", nullable = false)
    private PaymentProvider paymentProvider;

    @Column(name = "email_address")
    private String emailAddress;


}
