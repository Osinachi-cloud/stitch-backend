package com.stitch.wallet.repository;

import com.stitch.currency.model.enums.Currency;
import com.stitch.wallet.model.entity.BaseInflowWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BaseInflowWalletRepository extends JpaRepository<BaseInflowWallet, Long> {
    Optional<BaseInflowWallet> findByWalletId(String walletId);

    Optional<BaseInflowWallet> findByCurrency(Currency currency);
}
