package com.stitch.wallet.model.entity;


import com.stitch.currency.model.enums.Currency;
import com.stitch.wallet.model.enums.TransactionStatus;
import com.stitch.wallet.model.enums.TransactionType;
import com.stitch.commons.model.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "wallet_transaction")
public class WalletTransaction extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @Column(name = "transaction_id", nullable = false)
    private String transactionId; //16 unique digits

    @Column(name = "payment_transaction_id")
    private String paymentTransactionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private Currency currency;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "tran_type", nullable = false)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransactionStatus status;

    @Column(name = "description")
    private String description;

    @Column(name = "order_id")
    private String oderId;

}
