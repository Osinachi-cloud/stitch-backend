//package com.stitch.payment.repository;
//
//import com.stitch.currency.model.enums.Currency;
//import com.stitch.payment.model.entity.TierConfig;
//import com.stitch.payment.model.enums.TierActionType;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//
//@Repository
//public interface TierConfigRepository extends JpaRepository<TierConfig, Long> {
//
//    Optional<TierConfig> findByTierIgnoreCaseAndActionTypeAndCurrency(String tier, TierActionType actionType, Currency currency);
//}
