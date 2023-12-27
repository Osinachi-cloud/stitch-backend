package com.stitch.wallet.repository;

import com.stitch.currency.model.enums.Currency;
import com.stitch.wallet.model.entity.BaseInflowWalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BaseInflowWalletTransactionRepository extends JpaRepository<BaseInflowWalletTransaction, Long> {

    Optional<BaseInflowWalletTransaction> findByCurrency(Currency currency);
}
