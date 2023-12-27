package com.stitch.currency.service;

//import com.exquisapps.billanted.currency.model.dto.CurrencyDTO;
//import com.exquisapps.billanted.currency.model.dto.CurrencyRequest;
//import com.exquisapps.billanted.currency.model.enums.Currency;


import com.stitch.currency.model.dto.CurrencyDTO;
import com.stitch.currency.model.dto.CurrencyRequest;

import java.math.BigDecimal;
import java.util.List;



public interface CurrencyDetailsService {

    CurrencyDTO createCurrencyDetails(CurrencyRequest currencyRequest);

    List<CurrencyDTO> getAllCurrency();

    CurrencyDTO updateCurrencyMarkup(CurrencyRequest currencyRequest);

    CurrencyDTO updateCurrencyRate(CurrencyRequest currencyRequest);

}
