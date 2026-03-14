package com.stitch.user.model.dto;

import lombok.Data;

@Data
public class PasswordResetRequest {

    private String email;
    private String resetCode;
    private String password;
    private String confirmPassword;
}
