package com.stitch.payment.model.entity;

import com.stitch.commons.model.entity.BaseEntity;
import com.stitch.currency.model.enums.Currency;
import com.stitch.payment.model.enums.TierActionType;
import lombok.*;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tier_config")
public class TierConfig extends BaseEntity {

    @Column(name = "tier", nullable = false)
    private String tier;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(name = "action_type")
    @Enumerated(EnumType.STRING)
    private TierActionType actionType;
}
