package com.stitch.gateway.security.config;

import com.stitch.user.exception.UserException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

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
            System.out.println("no header");
        } else {

            String token = header.substring(7);

            try {
                Authentication authentication = tokenAuthenticationProvider.authenticate(token);

                SecurityContextHolder.getContext().setAuthentication(authentication);

                logger.trace("User token is valid and is authenticated");

            } catch (AuthenticationException ex) {
                logger.error("Token authentication error: " + ex.getMessage());
                SecurityContextHolder.clearContext();
                httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
                throw new UserException("Token authentication error: " + ex.getMessage());
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

}
