package com.stitch.payment.model.entity;

import com.stitch.commons.model.entity.BaseEntity;
import com.stitch.payment.model.enums.CardType;
//import com.stitch.psp.model.enums.PaymentProvider;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;


@Entity
@Getter
@Setter
@Table(name = "payment_card")
public class PaymentCard extends BaseEntity {


    @Column(name = "card_id", nullable = false, unique = true)
    private String cardId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "last4digits")
    private String last4digits;

    @Column(name = "first6digits")
    private String first6digits;

    @Column(name = "expiry")
    private String expiry;

    @Column(name = "card_type")
    @Enumerated(EnumType.STRING)
    private CardType cardType;

    @Column(name = "token", nullable = false)
    private String token;

//    @Enumerated(EnumType.STRING)
//    @Column(name = "provider")
//    private PaymentProvider paymentProvider;

    @Column(name = "fingerprint")
    private String fingerprint;

    @Column(name = "comments")
    private String comments;

}
