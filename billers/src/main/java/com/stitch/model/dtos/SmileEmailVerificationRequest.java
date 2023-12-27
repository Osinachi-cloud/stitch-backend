package com.stitch.model.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SmileEmailVerificationRequest {

    private String billersCode;
    private String serviceID;

}
