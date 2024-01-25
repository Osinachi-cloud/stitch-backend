package com.stitch.user.model.dto;

import com.stitch.user.enums.Tier;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.Instant;

public class VendorDto {

    private String firstName;

    private String lastName;

    private String middleName;

    private String emailAddress;

    private String phoneNumber;

    private Instant lastLogin;

    private boolean expiredPassword;

    private Instant lastPasswordChange;

    private boolean enabled = true;

    private int loginAttempts;

    private boolean accountLocked;

    private String nationality;

    private String businessName;

    private String profileImage;

    private String vendorId;

    private String pin;

    private Integer pinAttempts = 0;

    private Tier tier = Tier.ONE;

    private String country;

}
