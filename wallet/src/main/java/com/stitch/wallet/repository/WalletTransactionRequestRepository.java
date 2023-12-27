package com.stitch.wallet.repository;

import com.stitch.wallet.model.entity.WalletTransactionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletTransactionRequestRepository extends JpaRepository<WalletTransactionRequest, Long> {

    Optional<WalletTransactionRequest> findByTransactionId(String transactionId);
}
