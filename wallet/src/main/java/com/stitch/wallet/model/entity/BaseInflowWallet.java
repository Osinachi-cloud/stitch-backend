package com.stitch.wallet.model.entity;


import com.stitch.commons.model.entity.BaseEntity;
import com.stitch.currency.model.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "base_inflow_wallet")
public class BaseInflowWallet extends BaseEntity {


    @Column(name = "wallet_id", unique = true, nullable = false)
    private String walletId; // 5 unique digits

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private Currency currency;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "remarks")
    private String remarks;


}
