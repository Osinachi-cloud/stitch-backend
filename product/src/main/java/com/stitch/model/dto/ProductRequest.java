package com.stitch.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


@Data
public class ProductRequest {

    private String productId;

    private String provider;

    private String name;

    private String code;

    private String productImage;

    private BigDecimal price;

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

    private String expiryDate;

    private String sleeves;

    private String embroidery;

    private String colour;

    private String style;

    private String pattern;

    private String gender;

    private String productImage2;

    private String productImage3;

    private List<ProductVariationDto> productVariation;
}
