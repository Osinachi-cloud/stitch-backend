package com.stitch.gateway.security.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Token {

    private String accessToken;
    private String refreshToken;
}
