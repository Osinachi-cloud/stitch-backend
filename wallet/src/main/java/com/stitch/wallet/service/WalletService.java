package com.stitch.wallet.service;


import com.stitch.currency.model.enums.Currency;
import com.stitch.wallet.model.dto.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface WalletService {

    @Transactional
    WalletTransactionRequestDto initiateFunding(TransactionRequest transactionRequest);

    WalletTransactionRequestDto cancelFunding(String transactionId);

    @Transactional
    WalletDto createWallet(WalletRequest walletRequest);

    @Transactional
    WalletTransactionDto creditCustomerWallet(WalletCreditRequest creditRequest);

    @Transactional
    WalletTransactionDto debitCustomerWallet(WalletDebitRequest debitRequest);

    List<WalletDto> getAllWallets(String  customerId);

    @Transactional
    WalletTransactionRequestDto processFunding(TransactionRequest transactionRequest);

    @Transactional
    WalletTransactionDto reverseWalletDebitTransaction(WalletDebitReversalRequest debitReversalRequest);

    Currency getCustomerCurrency(String customerId);

    String getCustomerDefaultWalletId(String customerId);
}
