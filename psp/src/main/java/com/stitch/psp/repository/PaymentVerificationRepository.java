package com.stitch.psp.repository;

import com.stitch.psp.model.entity.PaymentVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentVerificationRepository extends JpaRepository<PaymentVerification, Long> {
}
