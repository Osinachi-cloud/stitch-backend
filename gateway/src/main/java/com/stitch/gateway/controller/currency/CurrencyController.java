package com.stitch.gateway.controller.currency;

import com.stitch.currency.model.dto.CurrencyDTO;
import com.stitch.currency.service.CurrencyDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.stitch.gateway.util.Constants.BASE_URL;

@RestController
@RequestMapping(BASE_URL)
public class CurrencyController {

    @Autowired
    private CurrencyDetailsService currencyDetailsService;

    @GetMapping("/getExchangeRates")
    public List<CurrencyDTO> getExchangeRates() {
        return  currencyDetailsService.getAllCurrency();
    }
}
