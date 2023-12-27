package com.stitch.wallet.repository;

import com.stitch.currency.model.enums.Currency;
import com.stitch.wallet.model.entity.BaseOutflowWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BaseOutflowWalletRepository extends JpaRepository<BaseOutflowWallet, Long> {
    Optional<BaseOutflowWallet> findByWalletId(String walletId);

    Optional<BaseOutflowWallet> findByCurrency(Currency currency);
}
