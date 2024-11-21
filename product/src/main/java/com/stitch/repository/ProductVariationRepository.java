package com.stitch.repository;

import com.stitch.model.entity.Product;
import com.stitch.model.entity.ProductVariation;
import com.stitch.user.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProductVariationRepository extends JpaRepository<ProductVariation, Long> {


}
