package com.stitch.model.dtos;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class VTPassElectricityPaymentRequest {
    private String customerId;
    private String orderId;
    private String request_id;
    private String serviceID;
    private String billersCode;
    private String variation_code;
    private BigDecimal amount;
    private String phone;
}
