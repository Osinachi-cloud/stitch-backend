package com.stitch.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ElectricityPaymentResponse extends BillPaymentResponse {
    private String code;
    private String description;
    private String productName;
    private String name;
    private String address;
    private String token;
    private String units;
    private String amount;
}
