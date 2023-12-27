package com.stitch.wallet.repository;

import com.stitch.wallet.model.entity.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {

    Optional<WalletTransaction> findByTransactionId(String transactionId);

}
