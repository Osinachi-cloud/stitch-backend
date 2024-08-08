package com.stitch.user.repository;

import com.stitch.user.model.entity.BodyMeasurement;
import com.stitch.user.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BodyMeasurementRepository extends JpaRepository<BodyMeasurement, Long> {
    Optional<BodyMeasurement> findByUserEntity(UserEntity customer);

    Optional<BodyMeasurement> findBodyMeasurementByIdAndUserEntity(Long id, UserEntity customer);

//    @Query("SELECT bm FROM BodyMeasurement bm WHERE bm.customer = :customer")
//    Optional<BodyMeasurement> findByCustomer(@Param("customer") Customer customer);
}
