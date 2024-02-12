package com.stitch.model.entity;

import com.stitch.commons.model.entity.BaseEntity;
import com.stitch.model.ProductCategory;
import com.stitch.user.model.entity.BodyMeasurement;
import com.stitch.user.model.entity.Vendor;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "product")
public class Product extends BaseEntity {

    @Column(name = "product_id")
    private String productId;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "product_image")
    private String productImage;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "quantity")
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
    @JoinColumn(name = "vendor_id", referencedColumnName = "id")
    private Vendor vendor;

}

