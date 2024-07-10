package com.stitch.repository;

import com.stitch.model.entity.Product;
import com.stitch.user.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    Optional<Product> findByProductId(String productId);


    Page<Product> findProductsByUserEntity(UserEntity customer, Pageable pageable);

//    Page<Product> findAllProductsByVendorId(Pageable pageable);


    void deleteByProductId(String productId);


//    Page<Product> findAll(Specification<Product> spec, PageRequest dateCreated);
}
