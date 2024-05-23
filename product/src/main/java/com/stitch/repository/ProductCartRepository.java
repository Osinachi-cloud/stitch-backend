package com.stitch.repository;

import com.stitch.model.entity.ProductCart;
import com.stitch.model.entity.ProductLike;
import com.stitch.user.model.entity.Customer;
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
    Page<ProductCart> findProductCartByCustomer(Customer customer, Pageable pageable);
    List<ProductCart> findProductCartByCustomer(Customer customer);


    Optional<ProductCart> findByProductId(String productId);

    @Query(value = "SELECT COUNT(*) FROM product_cart WHERE customer_id = :customerId", nativeQuery = true)
    int getCartCount(@Param("customerId") String customerId);

    @Query(value = "SELECT SUM(pc.amount_by_quantity) FROM product_cart pc WHERE pc.customer_id = :customerId", nativeQuery = true)
    BigDecimal sumAmountByQuantityByCustomerId(@Param("customerId") String customerId);
}
