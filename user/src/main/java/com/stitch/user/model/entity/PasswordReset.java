package com.stitch.user.model.entity;

import com.stitch.commons.model.entity.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@Table(name = "password_reset")
public class PasswordReset extends BaseEntity {

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "is_verified")
    private boolean verified;

    @Column(name = "reset_code")
    private String resetCode;

    @Column(name = "generated_on")
    private Instant generatedOn;

    @Column(name = "expired_on")
    private Instant expiredOn;

    @Column(name = "device_id")
    private String deviceId;
}
