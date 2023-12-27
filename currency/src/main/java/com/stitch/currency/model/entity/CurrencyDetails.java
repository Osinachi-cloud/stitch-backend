package com.stitch.currency.model.entity;

import com.stitch.currency.model.dto.CurrencyRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "currency_details")
public class CurrencyDetails extends BaseEntity {

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "currency_rate")
    private BigDecimal currencyRate;

    @Column(name = "currency_markup")
    private BigDecimal currencyMarkUp;


    public CurrencyDetails(CurrencyRequest currencyRequest) {

        this.currencyCode =  this.getCurrencyCode();
        this.currencyRate = this.getCurrencyRate();
        this.currencyMarkUp =  this.getCurrencyMarkUp();

    }

}
