package com.stitch.model.dto;

import com.stitch.model.ProductCategory;
import com.stitch.user.model.entity.Vendor;
import jakarta.persistence.*;
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

    private String category;

    private String provider;

    private boolean fixedPrice;

    private String country;

    private String vendor;
}
