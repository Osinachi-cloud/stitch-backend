package com.stitch.model.dto;

import com.stitch.model.ProductCategory;
import com.stitch.model.enums.PublishStatus;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductFilterRequest {

    private int page;
    private int size;
    private String name;

    private String code;

    private boolean outOfStock;

    private ProductCategory category;

    private String provider;

    private String vendorId;

    private PublishStatus publishStatus;

    private String productId;

}
