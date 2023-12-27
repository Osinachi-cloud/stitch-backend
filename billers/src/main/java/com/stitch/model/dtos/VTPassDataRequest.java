package com.stitch.model.dtos;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class VTPassDataRequest {

    private String customerId;
    private String orderId;
    private String serviceID;
    private String request_id;
    private String variation_code;
    private String billersCode;
    private String amount;
    private String phone;
}
