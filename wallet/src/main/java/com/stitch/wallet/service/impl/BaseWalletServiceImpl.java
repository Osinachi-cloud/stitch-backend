package com.stitch.wallet.service.impl;

import com.stitch.currency.model.enums.Currency;
import com.stitch.wallet.exception.WalletException;
import com.stitch.wallet.exception.WalletNotFoundException;
import com.stitch.wallet.exception.WalletTransactionException;
import com.stitch.wallet.model.entity.BaseInflowWalletTransaction;
import com.stitch.wallet.model.entity.BaseOutflowWalletTransaction;
import com.stitch.wallet.repository.BaseInflowWalletRepository;
import com.stitch.wallet.repository.BaseInflowWalletTransactionRepository;
import com.stitch.wallet.repository.BaseOutflowWalletRepository;
import com.stitch.wallet.repository.BaseOutflowWalletTransactionRepository;
import com.stitch.wallet.service.BaseWalletService;
import com.stitch.wallet.model.dto.BaseWalletCreditRequest;
import com.stitch.wallet.model.dto.BaseWalletDebitRequest;
import com.stitch.wallet.model.dto.WalletDebitReversalRequest;
import com.stitch.wallet.model.entity.BaseInflowWallet;
import com.stitch.wallet.model.entity.BaseOutflowWallet;
import com.stitch.wallet.model.enums.TransactionStatus;
import com.stitch.wallet.model.enums.TransactionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Slf4j
@Service
public class BaseWalletServiceImpl implements BaseWalletService {

    private final BaseInflowWalletRepository baseInflowWalletRepository;
    private final BaseOutflowWalletRepository baseOutflowWalletRepository;
    private final BaseInflowWalletTransactionRepository baseInflowWalletTransactionRepository;
    private final BaseOutflowWalletTransactionRepository baseOutflowWalletTransactionRepository;

    public BaseWalletServiceImpl(BaseInflowWalletRepository baseInflowWalletRepository, BaseOutflowWalletRepository baseOutflowWalletRepository, BaseInflowWalletTransactionRepository baseInflowWalletTransactionRepository, BaseOutflowWalletTransactionRepository baseOutflowWalletTransactionRepository) {
        this.baseInflowWalletRepository = baseInflowWalletRepository;
        this.baseOutflowWalletRepository = baseOutflowWalletRepository;
        this.baseInflowWalletTransactionRepository = baseInflowWalletTransactionRepository;
        this.baseOutflowWalletTransactionRepository = baseOutflowWalletTransactionRepository;
    }


    @Override
    @Transactional
    public void creditInflowBaseWallet(final BaseWalletCreditRequest creditRequest) {

        log.debug("Crediting inflow base wallet with request: {}", creditRequest);

        final BaseInflowWallet baseInflowWallet = getInflowWalletByCurrency(creditRequest.getCurrency());
        try {

            BaseInflowWalletTransaction baseInflowWalletTransaction = BaseInflowWalletTransaction.builder()
                    .baseInflowWallet(baseInflowWallet)
                    .userId(creditRequest.getCustomerId())
                    .customerWalletId(creditRequest.getWalletId())
                    .amount(creditRequest.getAmount())
                    .currency(creditRequest.getCurrency())
                    .transactionType(TransactionType.C)
                    .status(TransactionStatus.S)
                    .build();
            baseInflowWalletTransactionRepository.saveAndFlush(baseInflowWalletTransaction);

            log.info("Credited base wallet with request details: " + creditRequest);
        } catch (Exception e) {
            log.error("Error crediting base wallet", e);
            throw new WalletException("Failed to credit base wallet", e);
        }
    }

    @Override
    public void creditOutflowBaseWallet(final BaseWalletCreditRequest creditRequest) {

        log.debug("Crediting outflow base wallet with request: {}", creditRequest);

        final BaseOutflowWallet outflowWallet = getOutflowWalletByCurrency(creditRequest.getCurrency());

        try {

            BaseOutflowWalletTransaction walletTransaction = BaseOutflowWalletTransaction.builder()
                    .baseOutflowWallet(outflowWallet)
                    .userId(creditRequest.getCustomerId())
                    .customerWalletId(creditRequest.getWalletId())
                    .userWalletTransactionId(creditRequest.getCustomerWalletTransactionId())
                    .amount(creditRequest.getAmount())
                    .currency(creditRequest.getCurrency())
                    .transactionType(TransactionType.C)
                    .status(TransactionStatus.S)
                    .build();
            baseOutflowWalletTransactionRepository.saveAndFlush(walletTransaction);

            log.info("Credited outflow base wallet with request details: " + creditRequest);
        } catch (Exception e) {
            log.error("Error crediting outflow base wallet", e);
            throw new WalletException("Failed to credit outflow base wallet", e);
        }
    }


    @Override
    public void debitInflowBaseWallet(final BaseWalletDebitRequest debitRequest) {

        log.debug("Debiting inflow base wallet with request: {}", debitRequest);

        final BaseInflowWallet baseInflowWallet = getInflowWalletByCurrency(debitRequest.getCurrency());

        try {

            BaseInflowWalletTransaction baseInflowWalletTransaction = BaseInflowWalletTransaction.builder()
                    .baseInflowWallet(baseInflowWallet)
                    .customerWalletTransactionId(debitRequest.getCustomerWalletTransactionId())
                    .userId(debitRequest.getCustomerId())
                    .customerWalletId(debitRequest.getWalletId())
                    .amount(debitRequest.getAmount())
                    .currency(debitRequest.getCurrency())
                    .transactionType(TransactionType.D)
                    .status(TransactionStatus.S)
                    .build();
            baseInflowWalletTransactionRepository.saveAndFlush(baseInflowWalletTransaction);

            log.info("Debited inflow base wallet with request details: " + debitRequest);

        } catch (Exception e) {
            log.error("Error debiting inflow base wallet", e);
            throw new WalletException("Failed to debit inflow base wallet", e);
        }
    }

    @Override
    public void debitOutflowBaseWallet(final BaseWalletDebitRequest debitRequest) {

        log.debug("Debiting outflow base wallet with request: {}", debitRequest);

        final BaseOutflowWallet baseOutflowWallet = getOutflowWalletByCurrency(debitRequest.getCurrency());

        try {

            BaseOutflowWalletTransaction outflowWalletTransaction = BaseOutflowWalletTransaction.builder()
                    .baseOutflowWallet(baseOutflowWallet)
                    .userWalletTransactionId(debitRequest.getCustomerWalletTransactionId())
                    .userId(debitRequest.getCustomerId())
                    .customerWalletId(debitRequest.getWalletId())
                    .amount(debitRequest.getAmount())
                    .currency(debitRequest.getCurrency())
                    .transactionType(TransactionType.D)
                    .description(debitRequest.getDescription())
                    .status(TransactionStatus.S)
                    .build();
            baseOutflowWalletTransactionRepository.saveAndFlush(outflowWalletTransaction);

            log.info("Debited outflow base wallet with request details: " + debitRequest);

        } catch (Exception e) {
            log.error("Error debiting outflow base wallet", e);
            throw new WalletException("Failed to debit outflow base wallet", e);
        }
    }

    @Override
    public void reverseOutflowBaseWalletTransaction(WalletDebitReversalRequest debitReversalRequest) {
        log.debug("Reversing outflow base wallet transaction: {}", debitReversalRequest);

        BaseOutflowWalletTransaction outflowWalletTransaction = baseOutflowWalletTransactionRepository.findByUserWalletTransactionId(debitReversalRequest.getWalletTransactionId())
                .orElseThrow(() -> new WalletTransactionException("Could not find reference base outflow wallet transaction for customer wallet transactionId: " + debitReversalRequest.getWalletTransactionId()));

        if (!debitReversalRequest.getAmount().equals(outflowWalletTransaction.getAmount())){
            throw new WalletTransactionException("Debit reversal request amount does not match outflow base wallet transaction amount");
        }

        if (!debitReversalRequest.getCurrency().equals(outflowWalletTransaction.getCurrency())){
            throw new WalletTransactionException("Debit reversal request currency does not match outflow base wallet transaction currency");
        }

        if (!outflowWalletTransaction.getCustomerWalletId().equals(debitReversalRequest.getWalletId())){
            throw new WalletTransactionException("Debit reversal request customer wallet does not match that in outflow base wallet transaction");
        }

        BaseWalletDebitRequest baseWalletDebitRequest = BaseWalletDebitRequest.builder()
                .customerWalletTransactionId(debitReversalRequest.getWalletTransactionId())
                .customerId(outflowWalletTransaction.getUserId())
                .walletId(outflowWalletTransaction.getCustomerWalletId())
                .amount(outflowWalletTransaction.getAmount())
                .currency(outflowWalletTransaction.getCurrency())
                .description(debitReversalRequest.getDescription())
                .build();
        debitOutflowBaseWallet(baseWalletDebitRequest);
    }


    BaseInflowWallet getInflowWalletByCurrency(Currency currency) {
        return baseInflowWalletRepository.findByCurrency(currency)
                .orElseThrow(() -> new WalletNotFoundException("Base inflow wallet not found for currency: " + currency));
    }

    BaseOutflowWallet getOutflowWalletByCurrency(Currency currency) {
        return baseOutflowWalletRepository.findByCurrency(currency)
                .orElseThrow(() -> new WalletNotFoundException("Base outflow wallet not found for currency: " + currency));
    }
}
