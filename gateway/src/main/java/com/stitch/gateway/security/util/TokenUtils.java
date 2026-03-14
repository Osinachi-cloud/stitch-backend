package com.stitch.gateway.security.util;

import com.stitch.gateway.util.EnvProperties;
import com.stitch.user.model.dto.CustomerDto;
import com.stitch.gateway.security.model.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenUtils {
    private final EnvProperties props;

    public String generateAccessToken(CustomerDto user) {

        log.info("Inside generate token method 1");

        Claims claims = Jwts.claims()
                .setSubject(user.getUserId());
        claims.put("email", String.valueOf(user.getEmailAddress()));
        claims.put("role", "ROLE_" + user.getRole().getName());

        log.info("Inside generate token method 2");

        Date now = new Date();
        Date accessTokenExpiration = new Date(now.getTime() + props.getAccessTokenExpiryInMilliseconds());

        log.info("Inside generate token method 3");

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(accessTokenExpiration)
                .signWith(SignatureAlgorithm.HS512, props.getAccessTokenSecretKey())
                .compact();
    }


    public String generateRefreshToken(CustomerDto user) {

        Claims claims = Jwts.claims()
                .setSubject(user.getUserId());
        claims.put("role", "ROLE_CUSTOMER");

        Date now = new Date();
        Date refreshTokenExpiration = new Date(now.getTime() + props.getRefreshTokenExpiryInMilliseconds());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(refreshTokenExpiration)
                .signWith(SignatureAlgorithm.HS512, props.getRefreshTokenSecretKey())
                .compact();

    }

    public Token generateAccessAndRefreshToken(CustomerDto user) {
        return new Token(generateAccessToken(user), generateRefreshToken(user));
    }

    public Claims validateAccessToken(String token) {

        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(props.getAccessTokenSecretKey())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("Customer access token invalid");
            throw new BadCredentialsException("Invalid access token: " + e.getMessage());
        }
        return claims;
    }

    public Claims validateRefreshToken(String token) {

        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(props.getRefreshTokenSecretKey())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("Customer refresh token invalid", e);
            throw new BadCredentialsException("Invalid refresh token: " + e.getMessage());
        }
        return claims;
    }
}
