package com.stitch.wallet.model.dto;

import lombok.Data;

@Data
public class WalletFundingRequest {

    private String customerId;
    private String walletId;
    private double amount;
    private String currency;
}
