package com.stitch.wallet.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WalletRequest {

    private String customerId;
    private String name;
    private String currency;
    private String walletId;
    private BigDecimal amount;
    private String description;
    private Boolean isDefault;

    public WalletRequest(String customerId, String currency) {
        this.customerId = customerId;
        this.currency = currency;
    }

    public WalletRequest(String customerId, String currency, Boolean isDefault) {
        this.customerId = customerId;
        this.currency = currency;
        this.isDefault = isDefault;
    }


}
