package com.stitch.user.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private String country;
    private DeviceDto device;
    private String profileImage;
    private String currency;
    private String roleName;
    private String username;
    private String shortBio;

    @JsonProperty("isVendor")
    private boolean isVendor;
}
