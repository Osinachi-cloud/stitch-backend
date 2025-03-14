package com.stitch.model.dto;

import com.stitch.model.ProductCategory;
import com.stitch.model.enums.PublishStatus;
import lombok.Data;
import java.util.List;

@Data
public class ProductFilterRequest {

    private int page;
    private int size;
    private String name;
    private String code;
    private boolean outOfStock;
    private List<ProductCategory> categories; // Changed to List for multiple categories
    private String provider;
    private String vendorId;
    private PublishStatus publishStatus;
    private String productId;
    private Double minPrice;
    private Double maxPrice;
}
