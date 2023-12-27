package com.stitch.wallet.repository;

import com.stitch.wallet.model.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    List<Wallet> findByCustomerId(String customerId);

    Optional<Wallet> findByWalletId(String walletId);

    Optional<Wallet> findByCustomerIdAndIsDefaultTrue(String customerId);
}
