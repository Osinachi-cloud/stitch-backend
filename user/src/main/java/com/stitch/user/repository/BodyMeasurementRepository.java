package com.stitch.user.repository;

import com.stitch.user.model.entity.BodyMeasurement;
import com.stitch.user.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BodyMeasurementRepository extends JpaRepository<BodyMeasurement, Long> {
    Optional<BodyMeasurement> findByCustomer(Customer customer);

//    @Query("SELECT bm FROM BodyMeasurement bm WHERE bm.customer = :customer")
//    Optional<BodyMeasurement> findByCustomer(@Param("customer") Customer customer);
}
