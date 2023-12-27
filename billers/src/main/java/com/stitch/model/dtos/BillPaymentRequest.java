package com.stitch.model.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BillPaymentRequest {

    private String requestId;
    private String orderId;
    private String customerId;
    private String category;
    private String serviceId;
}
