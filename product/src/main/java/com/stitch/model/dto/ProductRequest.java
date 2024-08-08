package com.stitch.model.dto;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class ProductRequest {

    private String productId;

    private String provider;

    private String name;

    private String code;

    private String productImage;

    private BigDecimal amount;

    private BigDecimal quantity;

    private String category;

    private boolean fixedPrice;

    private String country;

    private String publishStatus;


    private String shortDescription;

    private String longDescription;

    private BigDecimal discount;

    private String materialUsed;

    private String readyIn;

}
