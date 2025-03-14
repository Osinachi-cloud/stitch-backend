package com.stitch.user.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    protected String firstName;
    protected String lastName;
    protected String middleName;
    protected String emailAddress;
    protected String phoneNumber;
    protected String lastLogin;
    protected boolean expiredPassword;
    protected String lastPasswordChange;
    protected boolean enabled;
    protected Integer loginAttempts;
    protected String nationality;
    protected String role;
    protected String profileImage;
    private String shortBio;

}
