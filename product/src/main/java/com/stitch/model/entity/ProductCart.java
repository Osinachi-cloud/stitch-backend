package com.stitch.model.entity;

import com.stitch.commons.model.entity.BaseEntity;
import com.stitch.user.model.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@ToString
@Table(name = "product_cart")
public class ProductCart extends BaseEntity {


    @Column(name = "productId")
    private String productId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "amount_by_quantity")
    private BigDecimal amountByQuantity;

    @OneToOne
    @JoinColumn(name = "email_address", referencedColumnName = "email_address")
    private UserEntity userEntity;

    @Column(name = "product_category_name")
    public String productCategoryName;

    @Column(name = "vendor_id")
    public String vendorId;
}
