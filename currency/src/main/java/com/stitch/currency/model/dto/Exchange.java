package com.stitch.currency.model.dto;

import com.stitch.currency.model.enums.Currency;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Exchange {

    private Currency currency;
    private BigDecimal rate;
    private BigDecimal totalAmount;
}
