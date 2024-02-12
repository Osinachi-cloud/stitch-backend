package com.stitch.repository;

import com.stitch.model.entity.Product;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByProductId(String productId);

    Page<Product> findProductsByVendorId(String id, Pageable pageable);

    void deleteByProductId(String productId);


}
