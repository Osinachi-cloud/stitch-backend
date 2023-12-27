package com.stitch.currency.repository;

import com.stitch.currency.model.entity.CurrencyDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyDetailsRepository extends JpaRepository<CurrencyDetails, Long> {

    CurrencyDetails findByCurrencyCode(String currencyCode);

}
