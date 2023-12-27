package com.stitch.wallet.service.impl;

import com.stitch.commons.enums.ResponseStatus;
import com.stitch.commons.util.NumberUtils;
import com.stitch.currency.model.enums.Currency;
import com.stitch.currency.service.ExchangeRateService;
//import com.stitch.notification.model.dto.InAppNotificationRequest;
//import com.stitch.notification.model.dto.PushRequest;
//import com.stitch.notification.model.enums.MessageSeverity;
//import com.stitch.notification.service.InAppNotificationService;
//import com.stitch.notification.service.NotificationService;
//import com.stitch.notification.service.PushNotificationService;
import com.stitch.wallet.exception.*;
import com.stitch.wallet.model.dto.*;
import com.stitch.wallet.model.entity.WalletTransaction;
import com.stitch.wallet.model.entity.WalletTransactionRequest;
import com.stitch.wallet.repository.WalletRepository;
import com.stitch.wallet.repository.WalletTransactionRequestRepository;
import com.stitch.wallet.service.BaseWalletService;
import com.stitch.wallet.service.WalletService;
import com.stitch.wallet.service.WalletTransactionService;
import com.stitch.wallet.model.entity.Wallet;
import com.stitch.wallet.model.enums.TransactionStatus;
import com.stitch.wallet.model.enums.TransactionType;
import com.stitch.wallet.model.enums.WalletStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletTransactionRequestRepository walletTransactionRequestRepository;
    private final BaseWalletService baseWalletService;
    private final WalletTransactionService walletTransactionsService;
    private final ExchangeRateService exchangeRateService;

//    private final NotificationService notificationService;

//    private final PushNotificationService pushNotificationService;

//    private final InAppNotificationService inAppNotificationService;

    public WalletServiceImpl(WalletRepository walletRepository,
                             WalletTransactionService walletTransactionsService,
                             BaseWalletService baseWalletService,
                             WalletTransactionRequestRepository walletTransactionRequestRepository,
                             ExchangeRateService exchangeRateService
//                             NotificationService notificationService,
//                             PushNotificationService pushNotificationService,
//                             InAppNotificationService inAppNotificationService
    ) {
        this.walletRepository = walletRepository;
        this.walletTransactionsService = walletTransactionsService;
        this.baseWalletService = baseWalletService;
        this.walletTransactionRequestRepository = walletTransactionRequestRepository;
        this.exchangeRateService = exchangeRateService;
//        this.notificationService = notificationService;
//        this.pushNotificationService = pushNotificationService;
//        this.inAppNotificationService = inAppNotificationService;
    }



    @Transactional
    @Override
    public WalletTransactionRequestDto initiateFunding(final TransactionRequest transactionRequest) {

        log.debug("Initiating wallet funding with request: {}", transactionRequest);

        if (transactionRequest.getAmount() == null || transactionRequest.getAmount().doubleValue() <= 0) {
            throw new InvalidWalletException("Invalid funding amount: " + transactionRequest.getAmount());
        }
        final String transactionId = walletTransactionsService.generateTransactionId();
        final Wallet wallet = getWalletEntity(transactionRequest.getWalletId());

        validateCustomerWallet(wallet, transactionRequest.getCustomerId(), Currency.valueOf(transactionRequest.getCurrency().toUpperCase()));

        WalletTransactionRequest walletTransactionRequest = WalletTransactionRequest.builder()
                .transactionId(transactionId)
                .amount(transactionRequest.getAmount())
                .currency(Currency.valueOf(transactionRequest.getCurrency()))
                .description(transactionRequest.getDescription())
                .transactionType(TransactionType.C)
                .status(TransactionStatus.P)
                .customerId(transactionRequest.getCustomerId())
                .walletId(transactionRequest.getWalletId())
                .build();

        walletTransactionRequest = this.walletTransactionRequestRepository.saveAndFlush(walletTransactionRequest);
        return WalletTransactionRequestDto.from(walletTransactionRequest);
    }

    private void validateCustomerWallet(final Wallet wallet, final String customerId, Currency currency) {

        if (!wallet.getCustomerId().equals(customerId)) {
            throw new InvalidWalletException(String.format("Wallet customerId [%s] does not match logged in customer ID [%s]", wallet.getCustomerId(), customerId));
        }

        if (!wallet.getStatus().equals(WalletStatus.A)) {
            throw new WalletStatusException(String.format("Cannot transact on non-active wallet. Wallet status is %s", wallet.getStatus().getDescription()));
        }

        if (!wallet.getCurrency().equals(currency)) {
            throw new InvalidWalletException(String.format("Wallet currency [%s] does not match provided currency [%s]", wallet.getCurrency(), currency));
        }
    }

    @Override
    public WalletTransactionRequestDto cancelFunding(final String transactionId) {

        log.debug("Cancelling wallet transaction with transactionId: {}", transactionId);

        walletTransactionsService.updateTransactionStatus(transactionId, TransactionStatus.C);
        WalletTransactionRequestDto transactionRequestDto = new WalletTransactionRequestDto();
        transactionRequestDto.setTransactionId(transactionId);
        transactionRequestDto.setStatus(TransactionStatus.C.getDescription());
        log.info("Cancelled wallet transaction with transactionId:: {}", transactionId);
        return transactionRequestDto;
    }

    private Wallet getWalletEntity(String walletId) {
        return walletRepository.findByWalletId(walletId)
                .orElseThrow(() -> new WalletNotFoundException(walletId));
    }

    @Transactional
    public WalletDto createWallet(final WalletRequest walletRequest) {

        log.debug("Creating wallet with request: {}", walletRequest);

        try {
            final String walletId = generateWalletId();
            Wallet wallet = Wallet.builder()
                    .walletId(walletId)
                    .customerId(walletRequest.getCustomerId())
                    .currency(Currency.valueOf(walletRequest.getCurrency()))
                    .balance(BigDecimal.ZERO)
                    .name(!StringUtils.isBlank(walletRequest.getName()) ? walletRequest.getName() : "Wallet" + walletId.substring(6))
                    .status(WalletStatus.A)
                    .isDefault(walletRequest.getIsDefault())
                    .build();
            wallet = this.walletRepository.saveAndFlush(wallet);
            log.info("Created wallet with ID: {}", walletId);
            return new WalletDto(wallet);
        } catch (Exception e) {
            log.error("Error creating wallet: " + walletRequest, e);
            throw new WalletException("Failed to create wallet for customer: " + walletRequest.getCustomerId(), e);
        }
    }

    @Transactional
    @Override
    public WalletTransactionDto creditCustomerWallet(final WalletCreditRequest creditRequest) {

        log.debug("Crediting customer wallet with request: {}", creditRequest);

        final Wallet wallet = getWalletEntity(creditRequest.getWalletId());

        validateCustomerWallet(wallet, creditRequest.getCustomerId(), creditRequest.getCurrency());

        final BigDecimal newWalletBalance = wallet.getBalance().add(creditRequest.getAmount());
        wallet.setBalance(newWalletBalance);

        try {

            this.walletRepository.saveAndFlush(wallet);

            WalletTransactionDto transactionDto = WalletTransactionDto.builder()
                    .amount(creditRequest.getAmount())
                    .transactionType(creditRequest.getTransactionType())
                    .status(TransactionStatus.S)
                    .description(creditRequest.getDescription())
                    .balance(wallet.getBalance())
                    .paymentTransactionId(creditRequest.getPaymentTransactionId())
                    .build();
            transactionDto = walletTransactionsService.createTransaction(wallet, transactionDto);

            BaseWalletCreditRequest baseWalletCreditRequest = BaseWalletCreditRequest.builder()
                    .customerId(creditRequest.getCustomerId())
                    .walletId(creditRequest.getWalletId())
                    .amount(creditRequest.getAmount())
                    .currency(creditRequest.getCurrency())
                    .build();
            baseWalletService.creditInflowBaseWallet(baseWalletCreditRequest);

            log.info("Credited customer wallet [{}] with amount [{}]", creditRequest.getWalletId(), creditRequest.getAmount());

            return transactionDto;
        } catch (WalletException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error crediting customer wallet", e);
            throw new WalletException("Failed to credit wallet: " + creditRequest.getWalletId(), e);
        }
    }


    @Transactional
    @Override
    public WalletTransactionDto debitCustomerWallet(final WalletDebitRequest debitRequest) {

        log.debug("Debiting wallet with request: {}", debitRequest);

        final Wallet wallet = getWalletEntity(debitRequest.getWalletId());

        final WalletDebitRequest updatedDebitRequest = convertToWalletCurrency(wallet, debitRequest);

        validateCustomerWallet(wallet, debitRequest.getCustomerId(), updatedDebitRequest.getCurrency());

        final BigDecimal balance = wallet.getBalance();
        final BigDecimal newBalance = balance.subtract(updatedDebitRequest.getAmount());

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientWalletBalanceException(ResponseStatus.INSUFFICIENT_WALLET_BALANCE.getMessage());
        }
        wallet.setBalance(newBalance);

        try {
            final Wallet updatedWallet = walletRepository.saveAndFlush(wallet);

            WalletTransactionDto transactionDto = WalletTransactionDto.builder()
                    .amount(updatedDebitRequest.getAmount())
                    .transactionType(updatedDebitRequest.getTransactionType())
                    .status(TransactionStatus.S)
                    .description(updatedDebitRequest.getDescription())
                    .orderId(updatedDebitRequest.getOrderId())
                    .transactionType(TransactionType.D)
                    .build();
            transactionDto = walletTransactionsService.createTransaction(wallet, transactionDto);

            BaseWalletDebitRequest baseWalletDebitRequest = BaseWalletDebitRequest.builder()
                    .customerWalletTransactionId(transactionDto.getTransactionId())
                    .customerId(updatedDebitRequest.getCustomerId())
                    .walletId(updatedDebitRequest.getWalletId())
                    .amount(updatedDebitRequest.getAmount())
                    .currency(updatedWallet.getCurrency())
                    .build();
            baseWalletService.debitInflowBaseWallet(baseWalletDebitRequest);

            BaseWalletCreditRequest baseWalletCreditRequest = BaseWalletCreditRequest.builder()
                    .customerWalletTransactionId(transactionDto.getTransactionId())
                    .customerId(updatedDebitRequest.getCustomerId())
                    .walletId(updatedDebitRequest.getWalletId())
                    .amount(updatedDebitRequest.getAmount())
                    .currency(updatedWallet.getCurrency())
                    .build();
            baseWalletService.creditOutflowBaseWallet(baseWalletCreditRequest);

            log.info("Successfully debited customer wallet with request details: " + updatedDebitRequest);
            return transactionDto;
        } catch (WalletException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error debiting wallet", e);
            throw new WalletException("Failed to debit wallet: " + updatedDebitRequest.getWalletId(), e);
        }
    }

    private WalletDebitRequest convertToWalletCurrency(Wallet wallet, WalletDebitRequest debitRequest) {

        if (debitRequest.getCurrency().equals(wallet.getCurrency())) {
            return debitRequest;
        }
        if (!wallet.getCurrency().equals(Currency.NGN)) {
            BigDecimal amount = exchangeRateService.getEquivalentCurrencyAmount(wallet.getCurrency(), debitRequest.getAmount());
            debitRequest.setAmount(amount);
            debitRequest.setCurrency(wallet.getCurrency());
        }

        if (wallet.getCurrency().equals(Currency.NGN)) {
            BigDecimal amount = exchangeRateService.getEquivalentNairaAmount(debitRequest.getCurrency(), debitRequest.getAmount());
            debitRequest.setAmount(amount);
            debitRequest.setCurrency(wallet.getCurrency());
        }
        return debitRequest;
    }


    public List<WalletDto> getAllWallets(final String customerId) {

        log.debug("Getting all wallets for customer: {}", customerId);
        List<Wallet> wallets = this.walletRepository.findByCustomerId(customerId);
        return wallets.stream()
                .map(WalletDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public WalletTransactionRequestDto processFunding(final TransactionRequest transactionRequest) {

        log.debug("Processing funding transaction: {}", transactionRequest);

        final WalletTransactionRequest walletTransactionRequest = walletTransactionsService.updateTransactionRequest(transactionRequest);

        if (walletTransactionRequest.getStatus().equals(TransactionStatus.S)) {

            WalletCreditRequest creditRequest = WalletCreditRequest.builder()
                    .walletId(walletTransactionRequest.getWalletId())
                    .currency(walletTransactionRequest.getCurrency())
                    .customerId(walletTransactionRequest.getCustomerId())
                    .transactionStatus(walletTransactionRequest.getStatus())
                    .amount(walletTransactionRequest.getAmount())
                    .paymentTransactionId(transactionRequest.getTxRef())
                    .transactionType(TransactionType.C)
                    .build();
            WalletTransactionDto transaction = creditCustomerWallet(creditRequest);
            WalletTransactionRequestDto transactionRequestDto = WalletTransactionRequestDto.from(walletTransactionRequest);
            transactionRequestDto.setTransactionId(transaction.getTransactionId());

//            sendNotifications(walletTransactionRequest, transaction, transactionRequest);

            return transactionRequestDto;

        } else if (walletTransactionRequest.getStatus().equals(TransactionStatus.F)) {

            Wallet wallet = walletRepository.findByWalletId(transactionRequest.getWalletId())
                    .orElseThrow(() -> new WalletNotFoundException(transactionRequest.getWalletId()));

            WalletTransactionDto transactionDto = WalletTransactionDto.builder()
                    .amount(transactionRequest.getAmount())
                    .transactionType(TransactionType.C)
                    .status(walletTransactionRequest.getStatus())
                    .description(transactionRequest.getDescription())
                    .paymentTransactionId(transactionRequest.getTxRef())
                    .build();
            WalletTransactionDto transaction = walletTransactionsService.createTransaction(wallet, transactionDto);
            WalletTransactionRequestDto transactionRequestDto = WalletTransactionRequestDto.from(walletTransactionRequest);
            transactionRequestDto.setTransactionId(transaction.getTransactionId());
            return transactionRequestDto;
        } else {
            return WalletTransactionRequestDto.from(walletTransactionRequest);
        }
    }

//    private void sendNotifications(WalletTransactionRequest walletTransactionRequest, WalletTransactionDto transaction, TransactionRequest transactionRequest) {
//        String content = String.format("Wallet successfully funded with %s%s", walletTransactionRequest.getCurrency(), NumberUtils.formatWithCommas(walletTransactionRequest.getAmount()));
//
//        if(transactionRequest.isEnablePush()){
//            pushNotificationService.sendPushNotification(PushRequest.builder()
//                .customerId(transactionRequest.getCustomerId())
//                .subject("Wallet Funding")
//                .content(content)
//                .build());
//        }
//
//        notificationService.walletFunding(new String[]{transactionRequest.getEmailAddress()},
//            String.format("%s%s", walletTransactionRequest.getCurrency(),NumberUtils.formatWithCommas(walletTransactionRequest.getAmount())),
//            String.format("%s%s", walletTransactionRequest.getCurrency(),NumberUtils.formatWithCommas(transaction.getBalance())),
//            transactionRequest.getFirstName());
//
//        inAppNotificationService.saveInAppNotification(InAppNotificationRequest.builder()
//                .customerId(transactionRequest.getCustomerId())
//                .subject("Wallet Funding")
//                .severity(MessageSeverity.L)
//                .content(content)
//            .build());
//
//    }

    @Transactional
    @Override
    public WalletTransactionDto reverseWalletDebitTransaction(WalletDebitReversalRequest debitReversalRequest){
        log.debug("Initiating reversal for customer wallet debit transaction: {}", debitReversalRequest);

        final Wallet wallet = getWalletEntity(debitReversalRequest.getWalletId());

        validateCustomerWallet(wallet, debitReversalRequest.getCustomerId(), debitReversalRequest.getCurrency());

        final WalletTransaction walletTransaction = walletTransactionsService.getTransaction(debitReversalRequest.getWalletTransactionId());

        if (!walletTransaction.getTransactionType().equals(TransactionType.D)){
            throw new WalletTransactionException("Cannot reverse a non-debit transaction");
        }

        if (!debitReversalRequest.getAmount().equals(walletTransaction.getAmount())){
            throw new WalletTransactionException("Debit reversal request amount does not match wallet transaction amount");
        }

        if (!debitReversalRequest.getCurrency().equals(walletTransaction.getCurrency())){
            throw new WalletTransactionException("Debit reversal request currency does not match wallet transaction currency");
        }

        if (!walletTransaction.getStatus().equals(TransactionStatus.S)){
            throw new WalletTransactionException("Cannot reverse a wallet debit transaction that is not successful");
        }

        baseWalletService.reverseOutflowBaseWalletTransaction(debitReversalRequest);

        WalletCreditRequest creditRequest = WalletCreditRequest.builder()
                .walletId(wallet.getWalletId())
                .currency(wallet.getCurrency())
                .customerId(wallet.getCustomerId())
                .transactionStatus(TransactionStatus.S)
                .amount(debitReversalRequest.getAmount())
                .transactionType(TransactionType.C)
                .description(debitReversalRequest.getDescription())
                .build();
        return creditCustomerWallet(creditRequest);
    }

    @Override
    public Currency getCustomerCurrency(String customerId) {
        Wallet wallet = walletRepository.findByCustomerIdAndIsDefaultTrue(customerId)
                .orElseThrow(() -> new WalletNotFoundException(" for default wallet with customerId: " + customerId));
        return wallet.getCurrency();
    }

    @Override
    public String getCustomerDefaultWalletId(String customerId) {
        Wallet wallet = walletRepository.findByCustomerIdAndIsDefaultTrue(customerId)
                .orElseThrow(() -> new WalletNotFoundException(" for default wallet with customerId: " + customerId));
        return wallet.getWalletId();
    }


    private String generateWalletId() {
        return NumberUtils.generate(10);
    }

}



