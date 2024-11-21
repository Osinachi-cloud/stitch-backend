package com.stitch.model.dto;

import lombok.Data;

@Data
public class ProductVariationRequest{
    private String color;
    private String sleeveType;
    private String measurementTag;
}