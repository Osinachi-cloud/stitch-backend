package com.stitch.model.entity;

import com.stitch.commons.model.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "product_variation")
public class ProductVariation extends BaseEntity {
    private String variationId;
    private String color;
    private String sleeveType;
}
