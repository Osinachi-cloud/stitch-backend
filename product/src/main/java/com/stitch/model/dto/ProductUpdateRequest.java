package com.stitch.model.dto;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class ProductUpdateRequest {

    private String name;

    private String productImage;

    private BigDecimal amount;

    private BigDecimal quantity;

    private String category;

    private boolean fixedPrice;

    private String code;

}
