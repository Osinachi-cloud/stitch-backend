package com.stitch.wallet.service.impl;


import com.stitch.commons.util.NumberUtils;
import com.stitch.wallet.exception.WalletTransactionException;
import com.stitch.wallet.repository.WalletTransactionRepository;
import com.stitch.wallet.repository.WalletTransactionRequestRepository;
import com.stitch.wallet.service.WalletTransactionService;
import com.stitch.wallet.model.dto.TransactionRequest;
import com.stitch.wallet.model.dto.WalletTransactionDto;
import com.stitch.wallet.model.entity.Wallet;
import com.stitch.wallet.model.entity.WalletTransaction;
import com.stitch.wallet.model.entity.WalletTransactionRequest;
import com.stitch.wallet.model.enums.TransactionStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class WalletTransactionServiceImpl implements WalletTransactionService {

    private final WalletTransactionRepository walletTransactionRepository;
    private final WalletTransactionRequestRepository walletTransactionRequestRepository;

    public WalletTransactionServiceImpl(WalletTransactionRepository walletTransactionRepository, WalletTransactionRequestRepository walletTransactionRequestRepository) {
        this.walletTransactionRepository = walletTransactionRepository;
        this.walletTransactionRequestRepository = walletTransactionRequestRepository;
    }


    @Override
    public WalletTransactionDto createTransaction(Wallet wallet, WalletTransactionDto transactionDto) {

        log.debug("Creating {} transaction for wallet [{}]", transactionDto.getTransactionType().getName(), wallet.getWalletId());
        WalletTransaction walletTransaction = WalletTransaction.builder()
                .wallet(wallet)
                .transactionId(generateTransactionId())
                .paymentTransactionId(transactionDto.getPaymentTransactionId())
                .currency(wallet.getCurrency())
                .amount(transactionDto.getAmount())
                .transactionType(transactionDto.getTransactionType())
                .status(transactionDto.getStatus())
                .description(transactionDto.getDescription())
                .oderId(transactionDto.getOrderId())
                .build();
        WalletTransaction transaction = walletTransactionRepository.saveAndFlush(walletTransaction);
        log.info("Created wallet transaction record: {}", transaction);
        transactionDto.setTransactionId(transaction.getTransactionId());
        return transactionDto;
    }

    @Override
    public void updateTransactionStatus(String transactionId, TransactionStatus transactionStatus) {

        WalletTransactionRequest walletTransactionRequest = walletTransactionRequestRepository.findByTransactionId(transactionId).orElseThrow(() -> new WalletTransactionException("Transaction not found: " + transactionId));
        walletTransactionRequest.setStatus(transactionStatus);
        walletTransactionRequestRepository.saveAndFlush(walletTransactionRequest);

        Optional<WalletTransaction> walletTransactionOptional = this.walletTransactionRepository.findByTransactionId(transactionId);
        if (walletTransactionOptional.isPresent()) {
            WalletTransaction walletTransaction = walletTransactionOptional.get();
            walletTransaction.setStatus(transactionStatus);
            this.walletTransactionRepository.saveAndFlush(walletTransaction);
        }
    }


    @Override
    public WalletTransactionRequest updateTransactionRequest(TransactionRequest transactionRequest){

        WalletTransactionRequest walletTransactionRequest = walletTransactionRequestRepository.findByTransactionId(transactionRequest.getTransactionId()).orElseThrow(() -> new WalletTransactionException("Transaction not found: " + transactionRequest.getTransactionId()));
        walletTransactionRequest.setPaymentTransactionId(transactionRequest.getTxRef());
        walletTransactionRequest.setStatus(transactionRequest.getStatus());
        return walletTransactionRequestRepository.saveAndFlush(walletTransactionRequest);
    }



    public String generateTransactionId() {
        return NumberUtils.generate(16);
    }

    @Override
    public WalletTransaction getTransaction(String walletTransactionId) {

        return walletTransactionRepository.findByTransactionId(walletTransactionId).
                orElseThrow(() -> new WalletTransactionException("Transaction not found: " + walletTransactionId));

    }
}
