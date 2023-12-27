package com.stitch.gateway.security.config;

import com.stitch.gateway.security.util.TokenUtils;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class TokenAuthenticationProvider {

    private final CustomUserDetailsService userDetailsService;
    private final TokenUtils tokenUtils;

    public TokenAuthenticationProvider(CustomUserDetailsService userDetailsService, TokenUtils tokenUtils) {
        this.userDetailsService = userDetailsService;
        this.tokenUtils = tokenUtils;
    }


    public Authentication authenticate(String token){
        Claims claims = tokenUtils.validateAccessToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername((String) claims.get("email"));

        if (!userDetails.isEnabled()){
            throw new DisabledException("User is disabled");
        }

        if (!userDetails.isAccountNonLocked()){
            throw new LockedException("User account is locked");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
