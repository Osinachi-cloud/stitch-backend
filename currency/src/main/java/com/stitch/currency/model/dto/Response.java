package com.stitch.currency.model.dto;

import com.stitch.currency.model.entity.CurrencyDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {

    private CurrencyDetails currencyDetails;
    private String message;

}
