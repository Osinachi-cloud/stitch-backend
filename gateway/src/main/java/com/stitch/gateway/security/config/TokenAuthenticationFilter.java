package com.stitch.gateway.security.config;

import com.stitch.user.model.dto.VendorDto;
import com.stitch.user.model.entity.Vendor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

//@Slf4j
//@Component
//public class TokenAuthenticationFilter extends OncePerRequestFilter {
//
//    private final TokenAuthenticationProvider tokenAuthenticationProvider;
//
//
//    public TokenAuthenticationFilter(TokenAuthenticationProvider tokenAuthenticationProvider) {
//        this.tokenAuthenticationProvider = tokenAuthenticationProvider;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
//
//        String header = httpServletRequest.getHeader("Authorization");
//
//        if (header == null || !header.startsWith("Bearer ")) {
//            log.trace("User attempting access without authentication token");
//        } else {
//
//            String token = header.substring(7);
//
//            log.info("my token :{}", token);
//
//            try {
//                System.out.println("hello world 1");
//                Authentication customerAuth = tokenAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(token, null, Collections.emptyList()));
//
//                log.info("customerAuth : {}", customerAuth);
//                Authentication vendorAuth = tokenAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(token, null, Collections.emptyList()));
//
//                System.out.println("hello world 1" + (Vendor) vendorAuth.getPrincipal());
//
//                SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
//                SecurityContextHolder.getContext().setAuthentication(vendorAuth);
//
//                logger.trace("User token is valid and is authenticated");
//            }
//            catch (Exception ex) {
//                logger.error("Token authentication error: " + ex.getMessage());
//                // guarantees the user is not authenticated at all
//                SecurityContextHolder.clearContext();
//                httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
//                return;
//            }
//        }
//        filterChain.doFilter(httpServletRequest, httpServletResponse);
//    }
//
//}



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
//                Authentication authentication = tokenAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(token, null, Collections.emptyList()));

                Authentication authentication = tokenAuthenticationProvider.authenticate(token);

                SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                logger.trace("User token is valid and is authenticated");

            } catch (AuthenticationException ex) {
                logger.error("Token authentication error: " + ex.getMessage());
                SecurityContextHolder.clearContext();
                httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
                return;
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
