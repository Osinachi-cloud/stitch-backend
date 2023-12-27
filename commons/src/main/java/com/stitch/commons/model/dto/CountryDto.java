package com.stitch.commons.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CountryDto {

    private String name;
    private String countryCode;
    private String currencyCode;
    private boolean active;
}
