package com.stitch.user.model.entity;


import com.stitch.user.enums.Tier;
import lombok.Data;

import jakarta.persistence.*;


@Data
@Entity
@Table(name = "customer")
public class Customer extends User {


    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "customer_id", unique = true, nullable = false)
    private String customerId;

    @Column(name = "pin")
    private String pin;

    @Column(name = "pin_attempts")
    protected Integer pinAttempts = 0;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "tier")
    private Tier tier = Tier.ONE;

    @Column(name = "country")
    private String country;

    @Column(name = "referred_by")
    private String referredBy;

    @Column(name = "save_card")
    private boolean saveCard;

    @Column(name = "enable_push")
    private boolean enablePush;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "customer_device", joinColumns = @JoinColumn(name = "customer_id"), inverseJoinColumns = @JoinColumn(name = "device_id"))
    private Device device;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "customer_address", joinColumns = @JoinColumn(name = "customer_id"), inverseJoinColumns = @JoinColumn(name = "address_id"))
    private Address address;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "customer_identity_document", joinColumns = @JoinColumn(name = "customer_id"), inverseJoinColumns = @JoinColumn(name = "identity_document_id"))
    private IdentityDocument identityDocument;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "body_measurement_id", referencedColumnName = "id")
    private BodyMeasurement bodyMeasurement;


}
