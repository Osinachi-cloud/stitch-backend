package com.stitch.model.entity;

import com.stitch.commons.model.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "product_variation")
public class ProductVariation extends BaseEntity {

    @Column(name = "product_id", nullable = false)
    private String productId;
    private String color;
    private String sleeveType;
}
