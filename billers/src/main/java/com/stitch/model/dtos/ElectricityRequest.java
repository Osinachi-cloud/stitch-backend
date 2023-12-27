package com.stitch.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ElectricityRequest {
    private String customerId;
    private String name;
    private String address;
    private String orderId;
    private String request_id;
    private String serviceID;
    private String billersCode;
    private String variation_code;
    private BigDecimal amount;
    private String phone;
}
