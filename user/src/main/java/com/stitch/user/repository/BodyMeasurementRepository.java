package com.stitch.user.repository;

import com.stitch.user.model.entity.BodyMeasurement;
import com.stitch.user.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BodyMeasurementRepository extends JpaRepository<BodyMeasurement, Long> {
    Optional<BodyMeasurement> findByUserEntity(UserEntity customer);

    Optional<BodyMeasurement> findBodyMeasurementByTagAndUserEntity(String id, UserEntity customer);

    List<BodyMeasurement> findBodyMeasurementByUserEntity(UserEntity customer);

    Optional<BodyMeasurement> findBodyMeasurementByTag(String tag);

}
