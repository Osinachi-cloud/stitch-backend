package com.stitch.currency.service.impl;

import com.stitch.currency.model.dto.Exchange;
import com.stitch.currency.model.entity.CurrencyDetails;
import com.stitch.currency.model.enums.Currency;
import com.stitch.currency.repository.CurrencyDetailsRepository;
import com.stitch.currency.service.ExchangeRateService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final CurrencyDetailsRepository currencyDetailsRepository;

    public ExchangeRateServiceImpl(CurrencyDetailsRepository currencyDetailsRepository) {
        this.currencyDetailsRepository = currencyDetailsRepository;
    }



    @Override
    public BigDecimal getExchangeRate(String currency){
        CurrencyDetails currencyDetails = currencyDetailsRepository.findByCurrencyCode(currency);
        return currencyDetails.getCurrencyRate().add(currencyDetails.getCurrencyMarkUp());
    }

    @Override
    public BigDecimal getEquivalentCurrencyAmount(Currency foreignCurrency, BigDecimal nairaAmount) {
        BigDecimal exchangeRate = getExchangeRate(foreignCurrency.name());
        return nairaAmount.divide(exchangeRate, 2, RoundingMode.HALF_UP);
    }

    @Override
    public Exchange getExchange(Currency foreignCurrency, BigDecimal nairaAmount) {
        BigDecimal exchangeRate = getExchangeRate(foreignCurrency.name());
        BigDecimal totalAmount = nairaAmount.divide(exchangeRate, 2, RoundingMode.HALF_UP);
        Exchange exchange = new Exchange();
        exchange.setRate(exchangeRate);
        exchange.setTotalAmount(totalAmount);
        exchange.setCurrency(foreignCurrency);
        return exchange;
    }

    @Override
    public BigDecimal getEquivalentNairaAmount(Currency currency, BigDecimal amount) {
        BigDecimal exchangeRate = getExchangeRate(currency.name());
        return amount.multiply(exchangeRate);
    }
}
