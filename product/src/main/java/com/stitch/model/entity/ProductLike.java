package com.stitch.model.entity;


import com.stitch.commons.model.entity.BaseEntity;
import com.stitch.user.model.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product_like")
public class ProductLike extends BaseEntity {

    @Column(name = "productId")
    private String productId;

    @OneToOne
    @JoinColumn(name = "email_address", referencedColumnName = "email_address")
    private UserEntity userEntity;
}
