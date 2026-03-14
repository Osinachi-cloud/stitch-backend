package com.stitch.user.model.dto;

import lombok.Data;


@Data
public class ChangePasswordRequest {
    private String oldPassword;
    private String password;
    private String confirmPassword;
}