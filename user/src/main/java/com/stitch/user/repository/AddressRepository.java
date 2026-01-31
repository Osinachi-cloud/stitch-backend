package com.stitch.user.repository;

import com.stitch.user.model.entity.Address;
import com.stitch.user.model.entity.BodyMeasurement;
import com.stitch.user.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;



public interface AddressRepository extends JpaRepository<Address, Long> {
}