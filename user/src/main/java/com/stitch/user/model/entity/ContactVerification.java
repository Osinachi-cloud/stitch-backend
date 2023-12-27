package com.stitch.user.model.entity;

import com.stitch.commons.model.entity.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "contact_verification")
public class ContactVerification extends BaseEntity {

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "is_verified")
    private boolean verified;

    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "generated_on")
    private Instant generatedOn;

    @Column(name = "expired_on")
    private Instant expiredOn;

    @Column(name = "device_id")
    private String deviceId;
}
