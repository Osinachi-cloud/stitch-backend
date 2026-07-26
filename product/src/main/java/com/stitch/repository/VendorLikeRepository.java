package com.stitch.repository;

import com.stitch.model.entity.VendorLike;
import com.stitch.user.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendorLikeRepository extends JpaRepository<VendorLike, Long> {
    Page<VendorLike> findVendorLikesByUserEntity(UserEntity customer, Pageable pageable);

    Optional<VendorLike> findByVendorEmailAddressAndUserEntity(String vendorEmailAddress, UserEntity customer);

    @Query(value = "SELECT COUNT(*) FROM vendor_like WHERE email_address = :emailAddress", nativeQuery = true)
    int getLikeCount(@Param("emailAddress") String emailAddress);
}
