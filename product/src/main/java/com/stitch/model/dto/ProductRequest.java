package com.stitch.model.dto;

import com.stitch.user.model.entity.Vendor;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class ProductRequest {

    private String productId;

    private String vendorId;

    private String name;

    private String code;

    private String productImage;

    private BigDecimal amount;

    private BigDecimal quantity;

    private String category;

    private boolean fixedPrice;

    private String country;

}
