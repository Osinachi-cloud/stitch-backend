package com.stitch.notification.repository;

import com.stitch.notification.model.PushNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PushNotificationRepository extends JpaRepository<PushNotification, Long> {

    Optional<PushNotification> findByToken(String token);

    List<PushNotification> findAllByCustomerId(String customerId);
}
