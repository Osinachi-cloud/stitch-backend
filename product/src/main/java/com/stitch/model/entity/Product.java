package com.stitch.model.entity;

import com.stitch.commons.model.entity.BaseEntity;
import com.stitch.model.ProductCategory;
import com.stitch.model.enums.PublishStatus;
import com.stitch.user.model.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "product")
public class Product extends BaseEntity {

    @Column(name = "product_id")
    private String productId;

    @Column(name = "name")
    private String name;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(name = "long_description")
    private String longDescription;

    @Column(name = "material_used")
    private String materialUsed;

    @Column(name = "ready_in")
    private String readyIn;

    @Column(name = "selling_price")
    @Min(value = 0, message = "Value cannot be negative")
    private BigDecimal sellingPrice;

    @Column(name = "code")
    private String code;

    @Column(name = "product_image")
    private String productImage;

    @Column(name = "amount")
    @Min(value = 0, message = "Value cannot be negative")
    private BigDecimal amount;

    @Column(name = "quantity")
    @Min(value = 0, message = "Value cannot be negative")
    private BigDecimal quantity;

    @Column(name = "out_of_stock")
    private boolean outOfStock;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    @Column(name = "provider")
    private String provider;

    @Column(name = "fixed_price")
    private boolean fixedPrice;

    @Column(name = "country")
    private String country;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserEntity userEntity;

    @Column(name = "publish_status",  nullable = false)
    @Enumerated(EnumType.STRING)
    private PublishStatus publishStatus;

    @Column(name = "discount")
    @Min(value = 0, message = "Value cannot be negative")
    private BigDecimal discount;

}

