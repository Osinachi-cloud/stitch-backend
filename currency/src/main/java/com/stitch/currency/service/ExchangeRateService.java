package com.stitch.currency.service;

import com.stitch.currency.model.dto.Exchange;
import com.stitch.currency.model.enums.Currency;

import java.math.BigDecimal;

public interface ExchangeRateService {
    BigDecimal getExchangeRate(String currency);

    BigDecimal getEquivalentCurrencyAmount(Currency currency, BigDecimal nairaAmount);

    Exchange getExchange(Currency currency, BigDecimal nairaAmount);

    BigDecimal getEquivalentNairaAmount(Currency currency, BigDecimal amount);
}
