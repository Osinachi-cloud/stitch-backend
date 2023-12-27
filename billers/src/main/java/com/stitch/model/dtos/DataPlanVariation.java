package com.stitch.model.dtos;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DataPlanVariation {
    private String variation_code;
    private String name;
    private String variation_amount;
    private String fixedPrice;
}
