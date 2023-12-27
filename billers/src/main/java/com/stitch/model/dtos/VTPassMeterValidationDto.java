package com.stitch.model.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VTPassMeterValidationDto {
    private String code;
    private Content content;

    @Data
    public static class Content {
        @JsonProperty("Customer_Name")
        private String customerName;
        @JsonProperty("Meter_Number")
        private String meterNumber;
        private String businessUnit;
        @JsonProperty("Address")
        private String address;
        private String customerArrears;
    }
}
