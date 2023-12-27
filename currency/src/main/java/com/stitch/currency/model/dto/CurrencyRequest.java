package com.stitch.currency.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CurrencyRequest {

    private String currencyCode;
    private BigDecimal currencyRate;
    private BigDecimal currencyMarkUp;
}
