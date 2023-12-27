package com.stitch.gateway.security.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenAuthenticationProvider tokenAuthenticationProvider;


    public TokenAuthenticationFilter(TokenAuthenticationProvider tokenAuthenticationProvider) {
        this.tokenAuthenticationProvider = tokenAuthenticationProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        String header = httpServletRequest.getHeader("Authorization");


        if (header == null || !header.startsWith("Bearer ")) {
            log.trace("User attempting access without authentication token");
        } else {

            String token = header.substring(7);

            try {
                Authentication auth = tokenAuthenticationProvider.authenticate(token);
                SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
                SecurityContextHolder.getContext().setAuthentication(auth);
                logger.trace("User token is valid and is authenticated");

            }
            catch (Exception ex) {
                logger.error("Token authentication error: "+ex.getMessage());
                // guarantees the user is not authenticated at all
                SecurityContextHolder.clearContext();
                httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
                return;
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

}
