package com.stitch.repository;

import com.stitch.model.entity.ProductOrder;
import com.stitch.model.enums.OrderStatus;
import jakarta.persistence.Column;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {


    @Query(value = "SELECT * FROM product_order " +
            "WHERE (:productId IS NULL OR product_id = :productId) " +
            "AND (:userId IS NULL OR user_id = :userId) " +
            "AND (:status IS NULL OR status = :status) " +
            "AND (:orderId IS NULL OR order_id = :orderId) " +
            "AND (:productCategoryName IS NULL OR product_category_name = :productCategoryName) " +
            "ORDER BY date_created ASC", nativeQuery = true)
    Page<ProductOrder> fetchCustomerOrdersBy(@Param("productId") String productId,
                                             @Param("userId") String userId,
                                             @Param("status") String status,
                                             @Param("orderId") String orderId,
                                             @Param("productCategoryName") String productCategoryName,
                                             PageRequest pr);



    Optional<ProductOrder> findByOrderId(String orderId);

    List<ProductOrder> findByUserId(String customerId);

    Optional<ProductOrder> findByProductId(String productId);

    Optional<ProductOrder> findByProductCategoryName(String productCategoryName);

    Optional<ProductOrder> findByVendorId(String productCategoryName);

    @Query("SELECT COUNT(p) FROM ProductOrder p WHERE p.userId = :userId")
    long countAllOrdersByCustomerId(@Param("userId") String userId);

    @Query("SELECT COUNT(p) FROM ProductOrder p WHERE p.userId = :userId AND p.status = 'FAILED'")
    long countFailedOrdersByCustomerId(@Param("userId") String userId);

    @Query("SELECT COUNT(p) FROM ProductOrder p WHERE p.userId = :userId AND p.status = 'CANCELLED'")
    long countCancelledOrdersByCustomerId(@Param("userId") String userId);

    @Query("SELECT COUNT(p) FROM ProductOrder p WHERE p.userId = :userId AND p.status = 'PROCESSING'")
    long countProcessingOrdersByCustomerId(@Param("userId") String userId);

    @Query("SELECT COUNT(p) FROM ProductOrder p WHERE p.userId = :userId AND p.status = 'COMPLETED'")
    long countCompletedOrdersByCustomerId(@Param("userId") String userId);
}
