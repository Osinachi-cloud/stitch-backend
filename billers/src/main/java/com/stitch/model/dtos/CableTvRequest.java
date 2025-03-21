package com.stitch.model.dtos;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CableTvRequest {

    @JsonIgnore
    private String customerId;
    @JsonIgnore
    private String orderId;
    private String request_id;
    private String serviceID;
    private String billersCode;
    private String variation_code;
    private String amount;
    private String phone;
    private String subscription_type;
    private String quantity;
}
