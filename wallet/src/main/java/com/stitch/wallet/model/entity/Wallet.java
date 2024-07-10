package com.stitch.wallet.model.entity;


import com.stitch.currency.model.enums.Currency;
import com.stitch.wallet.model.enums.WalletStatus;
import com.stitch.commons.model.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "wallet")
public class Wallet extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "wallet_id", unique = true, nullable = false)
    private String walletId; // 10 unique digits

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private Currency currency;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private WalletStatus status;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "is_default")
    private Boolean isDefault;

    @OneToMany(mappedBy = "wallet")
    private List<WalletTransaction> transactions;


    @Override
    public String toString() {
        return "Wallet{" +
                "customerId='" + userId + '\'' +
                ", walletId='" + walletId + '\'' +
                ", name='" + name + '\'' +
                ", currency=" + currency +
                ", balance=" + balance +
                ", status=" + status +
                ", remarks='" + remarks + '\'' +
                ", isDefault=" + isDefault +
                "} " + super.toString();
    }
}
