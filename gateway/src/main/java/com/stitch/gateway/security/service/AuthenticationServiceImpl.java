package com.stitch.gateway.security.service;

import com.stitch.commons.exception.StitchException;
import com.stitch.user.model.dto.CustomerDto;
import com.stitch.user.service.CustomerService;
import com.stitch.gateway.model.LoginRequest;
import com.stitch.gateway.model.LoginResponse;
import com.stitch.gateway.security.model.CustomUserDetails;
import com.stitch.gateway.security.model.Token;
import com.stitch.gateway.security.util.TokenUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final CustomerService customerService;
    private final TokenUtils tokenUtils;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, CustomerService customerService, TokenUtils tokenUtils) {
        this.authenticationManager = authenticationManager;
        this.customerService = customerService;
        this.tokenUtils = tokenUtils;
    }

    @Override
    public LoginResponse authenticate(LoginRequest loginRequest) {

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmailAddress(), loginRequest.getPassword()));

            CustomerDto user = getUser(authentication);

//            CustomerDto customerDto = customerService.getCustomerByEmail(user.getEmailAddress());

            Token token = tokenUtils.generateAccessAndRefreshToken(user);

            LoginResponse loginResponse = new LoginResponse(user, token);

            onSuccessfulAuthentication(user);
            return loginResponse;
        } catch (BadCredentialsException e) {
            log.error("Bad login credentials: " + loginRequest.getEmailAddress(), e);
            onFailedAuthentication(loginRequest.getEmailAddress(), e);
            throw new BadCredentialsException("Incorrect email address or password");
        } catch (AuthenticationException e) {
            log.error("Authentication error for user: " + loginRequest.getEmailAddress(), e);
            onFailedAuthentication(loginRequest.getEmailAddress(), e);
            if (e.getCause() != null) {
                Throwable cause = e.getCause();
                if (cause.getCause() != null) {
                    Throwable initialCause = cause.getCause();
                    throw new StitchException(initialCause.getMessage() != null ? initialCause.getMessage() : "Error processing request");
                }
                throw new StitchException(cause.getMessage() != null ? cause.getMessage() : "Error processing request");
            }
            throw new StitchException(e.getMessage() != null ? e.getMessage() : "Error processing request");
        }
    }

    private void onFailedAuthentication(String emailAddress, Throwable e) {
        customerService.updateLoginAttempts(emailAddress);
    }

    private void onSuccessfulAuthentication(CustomerDto user) {
        log.debug("Successful authentication for user: " + user.getCustomerId());
        customerService.updateLastLogin(user);
    }


    @Override
    public Token refreshAccessToken(String refreshToken) {

        Claims claims = tokenUtils.validateRefreshToken(refreshToken);
        CustomerDto customer = customerService.getCustomer(claims.getSubject());
        return tokenUtils.generateAccessAndRefreshToken(customer);
    }

    private CustomerDto getUser(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUser();
    }

    @Override
    public CustomerDto getAuthenticatedUser() {
        if (SecurityContextHolder.getContext() != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                return getUser(authentication);
            }
        }
        throw new AccessDeniedException("Unauthenticated user");
    }

    @Override
    public String getAuthenticatedCustomerId() {
        return getAuthenticatedUser().getCustomerId();
    }

}

