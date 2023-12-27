package com.stitch.gateway.model;

import lombok.Data;

@Data
public class LoginRequest {

    private String emailAddress;
    private String password;
}
