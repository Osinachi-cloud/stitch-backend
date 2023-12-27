package com.stitch.psp.repository;

import com.stitch.psp.model.entity.PspCustomerInfo;
import com.stitch.psp.model.enums.PaymentProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PspCustomerInfoRepository extends JpaRepository<PspCustomerInfo, Long> {
    Optional<PspCustomerInfo> findByBillantedCustomerIdAndPaymentProvider(String customerId, PaymentProvider paymentProvider);
}
