package com.stitch.payment.model.dto;

import com.stitch.currency.model.enums.Currency;
import com.stitch.payment.model.enums.TierActionType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DailyLimitRequest {
    private String customerId;
    private String tier;
    private String category;
    private TierActionType actionType;
    private BigDecimal loadAmount;
    private Currency currency;
}
