package com.stitch.user.model.dto;

import lombok.Data;

@Data
public class VerificationResponse {

    private Integer code;
    private String message;
}
