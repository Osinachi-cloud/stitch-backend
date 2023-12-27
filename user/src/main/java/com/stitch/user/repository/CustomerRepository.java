package com.stitch.user.repository;

import com.stitch.user.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByEmailAddress(String emailAddress);

    Optional<Customer> findByEmailAddress(String emailAddress);
    Optional<Customer> findByPhoneNumber(String phoneNumber);

    Optional<Customer> findByCustomerId(String customerId);
}
