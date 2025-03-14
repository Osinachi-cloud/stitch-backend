package com.stitch.gateway.security.service;

import com.stitch.gateway.model.LoginRequest;
import com.stitch.gateway.model.LoginResponse;
import com.stitch.gateway.security.model.Token;
import com.stitch.user.model.dto.CustomerDto;

public interface AuthenticationService {

    LoginResponse authenticate(LoginRequest loginRequest);
    Token refreshAccessToken(String token);
    CustomerDto getAuthenticatedUser();
    String getAuthenticatedCustomerId();
}
