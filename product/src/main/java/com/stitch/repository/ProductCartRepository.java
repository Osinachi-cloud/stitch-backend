package com.stitch.repository;

import com.stitch.model.entity.ProductCart;
import com.stitch.model.entity.ProductLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductCartRepository extends JpaRepository<ProductCart, Long> {
    Page<ProductCart> findProductCartByCustomerId(String id, Pageable pageable);

    Optional<ProductCart> findByProductId(String productId);

    @Query(value = "SELECT COUNT(*) FROM product_cart WHERE customer_id = :customerId", nativeQuery = true)
    int getCartCount(@Param("customerId") String customerId);
}
