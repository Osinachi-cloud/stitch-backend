package com.stitch.model.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataRequest {
    private String customerId;
    private String orderId;
    private String serviceID;
    private String request_id;
    private String variation_code;
    private String billersCode;
    private String amount;
    private String phone;
}
