package com.stitch.gateway.security.service;

//import com.exquisapps.billanted.gateway.model.LoginRequest;
//import com.exquisapps.billanted.gateway.model.LoginResponse;
//import com.exquisapps.billanted.gateway.security.model.Token;
import com.stitch.user.model.dto.CustomerDto;
import com.stitch.gateway.model.LoginRequest;
import com.stitch.gateway.model.LoginResponse;
import com.stitch.gateway.security.model.Token;

public interface AuthenticationService {

    LoginResponse authenticate(LoginRequest loginRequest);

    Token refreshAccessToken(String token);

    CustomerDto getAuthenticatedUser();
    String getAuthenticatedCustomerId();
}
