package com.stitch.model.dto;

import com.stitch.model.ProductCategory;
import com.stitch.model.enums.PublishStatus;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class CartDto {

    private String name;

    private String code;

    private String productImage;

    private BigDecimal amount;

    private BigDecimal quantity;

    private ProductCategory category;

    private boolean fixedPrice;

    private String vendor;

    private PublishStatus publishStatus;

    private BigDecimal discount;

    private String productId;

    private BigDecimal sellingPrice;

    private BigDecimal amountByQuantity;

}
