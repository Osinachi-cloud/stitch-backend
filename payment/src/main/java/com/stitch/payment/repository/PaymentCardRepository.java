package com.stitch.payment.repository;

import com.stitch.payment.model.entity.PaymentCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentCardRepository extends JpaRepository<PaymentCard, Long> {

    List<PaymentCard> findAllByCustomerId(String customerId);
    boolean existsByTokenAndCustomerId(String token, String customerId);
    boolean existsByFirst6digitsAndLast4digitsAndCustomerId(String firstSix, String lastFour, String customerId);
    boolean existsByFingerprintAndCustomerId(String fingerprint, String customerId);

    Optional<PaymentCard> findByCardId(String cardId);
    void deleteByCardId(String cardId);
}
