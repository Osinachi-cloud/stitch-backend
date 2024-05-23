package com.stitch.model.entity;

import com.stitch.commons.model.entity.BaseEntity;
import com.stitch.user.model.entity.Customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "product_cart")
public class ProductCart extends BaseEntity {

    @Column(name = "productId")
    private String productId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "amount_by_quantity")
    private BigDecimal amountByQuantity;

    @OneToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private Customer customer;
}
