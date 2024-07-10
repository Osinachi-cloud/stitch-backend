package com.stitch.wallet.model.dto;

import com.stitch.wallet.model.entity.Wallet;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class WalletDto {

    private String customerId;
    private String walletId;
    private String name;
    private String currency;
    private BigDecimal balance;
    private String status;
    private Boolean isDefault;



    public  WalletDto (Wallet wallet){
        this.customerId = wallet.getUserId();
        this.walletId  = wallet.getWalletId();
        this.name = wallet.getName();
        this.currency = wallet.getCurrency().toString();
        this.balance = wallet.getBalance();
        this.status = wallet.getStatus().toString();
        this.isDefault = wallet.getIsDefault();
    }
}
