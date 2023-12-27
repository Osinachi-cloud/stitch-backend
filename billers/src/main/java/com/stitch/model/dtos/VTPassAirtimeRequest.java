package com.stitch.model.dtos;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VTPassAirtimeRequest {

    @JsonProperty("serviceID")
    private String serviceId;
    @JsonProperty("request_id")
    private String requestId;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("phone")
    private String phone;
    @JsonIgnore
    private String orderId;
    @JsonIgnore
    private String customerId;

}
