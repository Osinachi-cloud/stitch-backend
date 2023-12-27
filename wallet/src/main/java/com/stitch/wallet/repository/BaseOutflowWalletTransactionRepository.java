package com.stitch.wallet.repository;

import com.stitch.currency.model.enums.Currency;
import com.stitch.wallet.model.entity.BaseOutflowWalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BaseOutflowWalletTransactionRepository extends JpaRepository<BaseOutflowWalletTransaction, Long> {

    Optional<BaseOutflowWalletTransaction> findByCurrency(Currency currency);

    Optional<BaseOutflowWalletTransaction> findByCustomerWalletTransactionId(String walletTransactionId);
}
