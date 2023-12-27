package com.stitch.model.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InternetEmailVerificationRequest {

    private String emailAddress;
    private String serviceId;

}
