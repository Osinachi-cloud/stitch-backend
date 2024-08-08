package com.stitch.model.dto;

import com.stitch.model.ProductCategory;
import com.stitch.model.enums.PublishStatus;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class ProductDto {

    private String name;

    private String code;

    private String productImage;

    private BigDecimal amount;

    private BigDecimal quantity;

    private boolean outOfStock;

    private ProductCategory category;

    private String provider;

    private boolean fixedPrice;

    private String country;

    private String vendor;

    private PublishStatus publishStatus;

    private BigDecimal discount;

    private String productId;

    private String shortDescription;

    private String longDescription;

    private String materialUsed;

    private String readyIn;

    private BigDecimal sellingPrice;

    private BigDecimal amountByQuantity;

    private boolean liked;
}
