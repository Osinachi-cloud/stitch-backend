package com.stitch.model.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VTPassElectricityMeterValidation {

    private String billersCode;
    private String serviceID;
    private String type;
}
