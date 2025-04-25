package com.stitch.gateway.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Email is required for login")
    private String emailAddress;
    @NotBlank(message = "Password is required for login")
    private String password;
}
