package com.stitch.wallet.model.entity;


import com.stitch.commons.model.entity.BaseEntity;
import com.stitch.currency.model.enums.Currency;
import com.stitch.wallet.model.enums.TransactionStatus;
import com.stitch.wallet.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;


@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wallet_transaction_request")
public class WalletTransactionRequest extends BaseEntity {

    @Column(name = "transaction_id", nullable = false)
    private String transactionId; //16 unique digits

    @Column(name = "ref_transaction_id")
    private String paymentTransactionId;

    @Column(name = "wallet_id")
    private String walletId;

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

    @Column(name = "customer_id", nullable = false)
    private String customerId;

}
