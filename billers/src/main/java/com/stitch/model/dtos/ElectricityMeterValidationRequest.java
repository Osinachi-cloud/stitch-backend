package com.stitch.model.dtos;

import lombok.Data;

@Data
public class ElectricityMeterValidationRequest {

    private String number;
    private String serviceID;
    private String type;

}
