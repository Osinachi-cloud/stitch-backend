package com.stitch.payment.model.dto;

import com.stitch.currency.model.enums.Currency;
import com.stitch.payment.model.enums.TierActionType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TierConfigDto {
    private String tier;
    private BigDecimal amount;
    private Currency currency;
    private TierActionType actionType;
}
