package com.stitch.gateway.model.response;

import lombok.Data;

@Data
public class ErrorResponse {
    private Integer code;
    private String message;
}
