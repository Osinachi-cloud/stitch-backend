package com.stitch.currency.util;


import com.stitch.currency.model.dto.CurrencyRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CurrencyValidator {

    public List<String> validateCurrencyRequest(CurrencyRequest currencyRequest, boolean validateMarkup) {
        List<String> errors = new ArrayList<>();

        String currencyCodeError = validateCurrencyCode(currencyRequest.getCurrencyCode());
        if (currencyCodeError  != null) {
            errors.add(currencyCodeError);
        }

        String currencyRateError  = validateCurrencyRate(currencyRequest.getCurrencyRate());
        if (currencyRateError  != null) {
            errors.add(currencyRateError);
        }

        if(validateMarkup) {
            String currencyMarkUpError  = validateCurrencyMarkUp(currencyRequest.getCurrencyMarkUp());
            if (currencyMarkUpError != null) {
                errors.add(currencyRateError);
            }
        }




        return errors;
    }


    private String validateCurrencyCode(String currencyCode) {
        if (currencyCode == null || currencyCode.isEmpty()) {
            return "CurrencyCode cannot be null or empty";
        }
        return null;
    }

    private String validateCurrencyRate(BigDecimal currencyRate) {
        if (currencyRate == null || currencyRate.compareTo(BigDecimal.ZERO) <= 0) {
            return "CurrencyRate cannot be null or empty";
        }
        return null;
    }

    private String validateCurrencyMarkUp(BigDecimal currencyMarkUp) {
        if (currencyMarkUp == null || currencyMarkUp.compareTo(BigDecimal.ZERO) <= 0) {
            return " CurrencyMarkUp cannot be null or empty";
        }
        return null;
    }




}
