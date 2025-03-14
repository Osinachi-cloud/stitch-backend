package com.stitch.gateway.controller.currency;

import com.stitch.currency.model.dto.CurrencyDTO;
import com.stitch.currency.service.CurrencyDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CurrencyController {

    @Autowired
    private CurrencyDetailsService currencyDetailsService;

    @QueryMapping(value = "getExchangeRates")
    public List<CurrencyDTO> getExchangeRates() {
        return  currencyDetailsService.getAllCurrency();
    }
}
