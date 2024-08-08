package com.stitch.user.model.entity;


import com.stitch.user.enums.Tier;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;



@Getter
@Setter
@Audited
@Entity
@Table(name = "USER_ENTITY")
public class UserEntity extends User {

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "user_id", unique = true, nullable = false)
    private String userId;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", referencedColumnName = "id")
    private Device device;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "identity_document_id", referencedColumnName = "id")
    private IdentityDocument identityDocument;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "body_measurement_id", referencedColumnName = "id")
//    private BodyMeasurement bodyMeasurement;

}
