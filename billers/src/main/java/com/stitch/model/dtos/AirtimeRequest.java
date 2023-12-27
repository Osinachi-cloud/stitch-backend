package com.stitch.model.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AirtimeRequest {

    private String serviceID;
    private String request_id;
    private String amount;
    private String phone;
    private String orderId;
    private String customerId;
}
