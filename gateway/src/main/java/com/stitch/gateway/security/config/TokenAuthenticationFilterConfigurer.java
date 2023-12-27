package com.stitch.gateway.security.config;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class TokenAuthenticationFilterConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final TokenAuthenticationProvider tokenAuthenticationProvider;

    public TokenAuthenticationFilterConfigurer(TokenAuthenticationProvider tokenAuthenticationProvider) {
        this.tokenAuthenticationProvider = tokenAuthenticationProvider;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        TokenAuthenticationFilter customFilter = new TokenAuthenticationFilter(tokenAuthenticationProvider);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}