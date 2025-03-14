package com.stitch.repository;

import com.stitch.model.entity.ProductVariation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductVariationRepository extends JpaRepository<ProductVariation, Long> {
    List<ProductVariation> findProductVariationByProductId(String productId);

}
