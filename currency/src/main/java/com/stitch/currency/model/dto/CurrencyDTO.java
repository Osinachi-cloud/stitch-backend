package com.stitch.currency.model.dto;

import com.stitch.currency.model.entity.CurrencyDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyDTO {

    private String currencyCode;
    private BigDecimal currencyRate;

    private boolean success;

    private String message;

    public CurrencyDTO(CurrencyDetails currencyDetails){
        this.currencyCode = currencyDetails.getCurrencyCode();
        this.currencyRate =  currencyDetails.getCurrencyRate().add(currencyDetails.getCurrencyMarkUp());
        this.success = currencyDetails.getCurrencyRate() != null;
    }
}
