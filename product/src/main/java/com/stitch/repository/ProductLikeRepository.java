package com.stitch.repository;


import com.stitch.model.entity.Product;
import com.stitch.model.entity.ProductLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductLikeRepository extends JpaRepository<ProductLike, Long> {
    Page<ProductLike> findProductLikesByCustomerId(String id, Pageable pageable);

    Optional<ProductLike> findByProductId(String productId);

    @Query(value = "SELECT COUNT(*) FROM product_like WHERE customer_id = :customerId", nativeQuery = true)
    int getLikeCount(@Param("customerId") String customerId);
}
