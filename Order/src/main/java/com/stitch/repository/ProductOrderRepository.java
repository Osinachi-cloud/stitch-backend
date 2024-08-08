package com.stitch.repository;

import com.stitch.model.entity.ProductOrder;
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
            "AND (:emailAddress IS NULL OR email_address = :emailAddress) " +
            "AND (:status IS NULL OR status = :status) " +
            "AND (:orderId IS NULL OR order_id = :orderId) " +
            "AND (:productCategoryName IS NULL OR product_category_name = :productCategoryName) " +
            "ORDER BY date_created ASC", nativeQuery = true)
    Page<ProductOrder> fetchCustomerOrdersBy(@Param("productId") String productId,
                                             @Param("emailAddress") String emailAddress,
                                             @Param("status") String status,
                                             @Param("orderId") String orderId,
                                             @Param("productCategoryName") String productCategoryName,
                                             PageRequest pr);


    @Query(value = "SELECT * FROM product_order " +
            "WHERE (:productId IS NULL OR product_id = :productId) " +
            "AND (:emailAddress IS NULL OR vendor_email_address = :emailAddress) " +
            "AND (:status IS NULL OR status = :status) " +
            "AND (:orderId IS NULL OR order_id = :orderId) " +
            "AND (:productCategoryName IS NULL OR product_category_name = :productCategoryName) " +
            "ORDER BY date_created ASC", nativeQuery = true)
    Page<ProductOrder> fetchVendorOrdersBy(@Param("productId") String productId,
                                             @Param("emailAddress") String emailAddress,
                                             @Param("status") String status,
                                             @Param("orderId") String orderId,
                                             @Param("productCategoryName") String productCategoryName,
                                             PageRequest pr);



    Optional<ProductOrder> findByOrderId(String orderId);

    List<ProductOrder> findProductOrdersByTransactionId(String orderId);

    List<ProductOrder> findByEmailAddress(String emailAddress);

    List<ProductOrder> findProductOrderByVendorEmailAddress(String emailAddress);

    Optional<ProductOrder> findByProductId(String productId);

    Optional<ProductOrder> findByProductCategoryName(String productCategoryName);

    Optional<ProductOrder> findByVendorEmailAddress(String productCategoryName);

    @Query("SELECT COUNT(p) FROM ProductOrder p WHERE p.emailAddress = :emailAddress")
    long countAllOrdersByCustomerId(@Param("emailAddress") String emailAddress);

    @Query("SELECT COUNT(p) FROM ProductOrder p WHERE p.emailAddress = :emailAddress AND p.status = 'FAILED'")
    long countFailedOrdersByCustomerId(@Param("emailAddress") String emailAddress);

    @Query("SELECT COUNT(p) FROM ProductOrder p WHERE p.emailAddress = :emailAddress AND p.status = 'CANCELLED'")
    long countCancelledOrdersByCustomerId(@Param("emailAddress") String emailAddress);

    @Query("SELECT COUNT(p) FROM ProductOrder p WHERE p.emailAddress = :emailAddress AND p.status = 'PROCESSING'")
    long countProcessingOrdersByCustomerId(@Param("emailAddress") String emailAddress);

    @Query("SELECT COUNT(p) FROM ProductOrder p WHERE p.emailAddress = :emailAddress AND p.status = 'COMPLETED'")
    long countCompletedOrdersByCustomerId(@Param("emailAddress") String emailAddress);



    @Query("SELECT COUNT(p) FROM ProductOrder p WHERE p.vendorEmailAddress = :emailAddress")
    long countAllOrdersByVendorId(@Param("emailAddress") String emailAddress);

    @Query("SELECT COUNT(p) FROM ProductOrder p WHERE p.vendorEmailAddress = :emailAddress AND p.status = 'FAILED'")
    long countFailedOrdersByVendorId(@Param("emailAddress") String emailAddress);

    @Query("SELECT COUNT(p) FROM ProductOrder p WHERE p.vendorEmailAddress = :emailAddress AND p.status = 'CANCELLED'")
    long countCancelledOrdersByVendorId(@Param("emailAddress") String emailAddress);

    @Query("SELECT COUNT(p) FROM ProductOrder p WHERE p.vendorEmailAddress = :emailAddress AND p.status = 'PROCESSING'")
    long countProcessingOrdersByVendorId(@Param("emailAddress") String emailAddress);

    @Query("SELECT COUNT(p) FROM ProductOrder p WHERE p.vendorEmailAddress = :emailAddress AND p.status = 'COMPLETED'")
    long countCompletedOrdersByVendorId(@Param("emailAddress") String emailAddress);
}
