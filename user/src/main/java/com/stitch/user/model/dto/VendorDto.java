package com.stitch.user.model.dto;

import com.stitch.user.enums.Tier;
import com.stitch.user.model.entity.Vendor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VendorDto {

    private String firstName;

    private String lastName;

    private String middleName;

    private String password;

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

    private String tier;

    private String country;

    public VendorDto(Vendor vendor){
        this.vendorId = vendor.getVendorId();
        this.firstName = vendor.getFirstName();
        this.lastName = vendor.getLastName();
        this.emailAddress = vendor.getEmailAddress();
        this.phoneNumber = vendor.getPhoneNumber();
        this.tier = vendor.getTier().name();
        this.country = vendor.getCountry();
        this.profileImage = vendor.getProfileImage();
    }

}
