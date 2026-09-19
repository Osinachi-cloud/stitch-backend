package com.stitch.model.dto;

import com.stitch.model.ProductCategory;
import com.stitch.model.enums.PublishStatus;
import com.stitch.user.model.dto.UserDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


@Data
public class ProductDto {

    private String name;

    private String code;

    private String productImage;

    private BigDecimal price;

    private BigDecimal quantity;

    private boolean outOfStock;

    private ProductCategory category;

    private String provider;

    private boolean fixedPrice;

    private String country;

//    private String vendor;

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

    private UserDto vendor;

    private String expiryDate;

    private String sleeves;

    private String embroidery;

    private String colour;

    private String style;

    private String pattern;

    private String gender;

    private String productImage2;

    private String productImage3;

    List<ProductVariationDto> productVariation;
}
