package com.stitch.payment.model.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CableRequest {
    private String request_id;
    private String serviceID;
    private String billersCode;
    private String variation_code;
    private String amount;
    private String phone;
    private String subscription_type;
    private String quantity;
}
