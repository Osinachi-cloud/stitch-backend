
package com.stitch.user.repository;

import com.stitch.user.model.entity.BodyMeasurement;
import com.stitch.user.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface BodyMeasurementRepository extends JpaRepository<BodyMeasurement, Long> {
    Optional<BodyMeasurement> findByUserEntity(UserEntity customer);

    Optional<BodyMeasurement> findBodyMeasurementByTagAndUserEntity(String id, UserEntity customer);

    List<BodyMeasurement> findBodyMeasurementByUserEntity(UserEntity customer);

    Optional<BodyMeasurement> findBodyMeasurementByTag(String tag);

    @Modifying
    @Transactional
    @Query("DELETE FROM BodyMeasurement bm WHERE bm.tag = :tag AND bm.userEntity.emailAddress = :email")
    void deleteBodyMeasurementByTagAndUserEmail(@Param("tag") String tag, @Param("email") String email);

    Optional<BodyMeasurement> findBodyMeasurementByTagAndUserEntity_EmailAddress(String tag, String email);

//    @Modifying
//    @Query("UPDATE BodyMeasurement bm SET bm.isDefault = false WHERE bm.userEntity.id = :userId")
//    void updateAllUserMeasurementsToNonDefault(@Param("userId") Long userId);

    boolean existsByUserEntityAndIsDefaultTrue(UserEntity userEntity);

    @Query("SELECT COUNT(bm) FROM BodyMeasurement bm WHERE bm.userEntity = :userEntity AND bm.isDefault = true")
    long countByUserEntityAndIsDefaultTrue(@Param("userEntity") UserEntity userEntity);

    @Modifying
    @Query("UPDATE BodyMeasurement bm SET bm.isDefault = true WHERE bm.userEntity.id = :userId AND bm.id = (SELECT MIN(bm2.id) FROM BodyMeasurement bm2 WHERE bm2.userEntity.id = :userId)")
    void setOldestMeasurementAsDefault(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE BodyMeasurement bm SET bm.isDefault = false WHERE bm.userEntity.emailAddress = :email")
    void updateAllUserMeasurementsToNonDefault(@Param("email") String email);
}
