package com.stitch.gateway.security.config;

import com.stitch.gateway.security.util.TokenUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class TokenAuthenticationProvider {

    @Qualifier("customUserDetailsService")
    private final CustomUserDetailsServiceImpl customUserDetailsService;


    private final TokenUtils tokenUtils;

    public TokenAuthenticationProvider(@Qualifier("customUserDetailsService") CustomUserDetailsServiceImpl customUserDetailsService, TokenUtils tokenUtils) {
        this.customUserDetailsService = customUserDetailsService;
        this.tokenUtils = tokenUtils;
    }

    public Authentication authenticate(String token) {
        Claims claims = tokenUtils.validateAccessToken(token);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername((String) claims.get("email"));

        if (!userDetails.isEnabled()) {
            throw new DisabledException("User is disabled");
        }

        if (!userDetails.isAccountNonLocked()) {
            throw new LockedException("User account is locked");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}