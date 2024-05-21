package com.stitch.model.entity;


import com.stitch.commons.model.entity.BaseEntity;
import com.stitch.user.model.entity.Customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "product_like")
public class ProductLike extends BaseEntity {

    @Column(name = "productId")
    private String productId;

    @OneToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private Customer customer;
}
