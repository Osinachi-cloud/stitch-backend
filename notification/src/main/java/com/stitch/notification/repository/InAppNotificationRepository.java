package com.stitch.notification.repository;

import com.stitch.notification.model.InAppNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InAppNotificationRepository extends JpaRepository<InAppNotification, Long> {
    Optional<InAppNotification> findByCustomerIdAndNotificationId(String customerId, String notificationId);

    void deleteByNotificationId(String notificationId);

    Page<InAppNotification> findAllByCustomerId(String customerId, Pageable pageable);
    long countAllByCustomerId(String customerId);
    long countAllByCustomerIdAndRead(String customerId, boolean read);
}
