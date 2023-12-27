package com.stitch.psp.model.dto.stripe;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerRequest {

    private String customerId;
    private String emailAddress;
    private String fullName;
}
