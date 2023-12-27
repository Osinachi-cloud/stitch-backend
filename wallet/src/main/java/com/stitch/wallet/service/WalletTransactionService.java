package com.stitch.wallet.service;

import com.stitch.wallet.model.dto.TransactionRequest;
import com.stitch.wallet.model.dto.WalletTransactionDto;
import com.stitch.wallet.model.entity.Wallet;
import com.stitch.wallet.model.entity.WalletTransaction;
import com.stitch.wallet.model.entity.WalletTransactionRequest;
import com.stitch.wallet.model.enums.TransactionStatus;

public interface WalletTransactionService {

    WalletTransactionDto createTransaction(Wallet wallet, WalletTransactionDto walletTransactionDto);

    void updateTransactionStatus(String transactionId, TransactionStatus transactionStatus);

    WalletTransactionRequest updateTransactionRequest(TransactionRequest transactionRequest);

    String generateTransactionId();

    WalletTransaction getTransaction(String walletTransactionId);
}
