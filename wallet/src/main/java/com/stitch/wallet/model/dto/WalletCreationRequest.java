package com.stitch.wallet.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletCreationRequest {

    private String customerId;
    private String country;
    private String currency;
    private String walletName;

}
