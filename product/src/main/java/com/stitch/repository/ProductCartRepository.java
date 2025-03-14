package com.stitch.repository;

import com.stitch.model.entity.ProductCart;
import com.stitch.user.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductCartRepository extends JpaRepository<ProductCart, Long> {
    Page<ProductCart> findProductCartByCustomer(UserEntity customer, Pageable pageable);
    List<ProductCart> findProductCartByCustomer(UserEntity customer);
    Optional<ProductCart> findByProductId(String productId);

    Optional<ProductCart> findByProductIdAndColorAndSleeveTypeAndMeasurementTag(String productId, String color, String sleeveType, String measurementType);

    @Query(value = "SELECT COUNT(*) FROM product_cart WHERE email_address = :emailAddress", nativeQuery = true)
    int getCartCount(@Param("emailAddress") String emailAddress);

    @Query(value = "SELECT SUM(pc.amount_by_quantity) FROM product_cart pc WHERE pc.email_address = :emailAddress", nativeQuery = true)
    BigDecimal sumAmountByQuantityByUserId(@Param("emailAddress") String emailAddress);
}
