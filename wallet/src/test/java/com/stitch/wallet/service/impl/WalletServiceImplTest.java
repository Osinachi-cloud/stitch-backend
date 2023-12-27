//package com.stitches.wallet.service.impl;
//
//import com.exquisapps.billanted.commons.enums.ResponseStatus;
//import com.exquisapps.billanted.commons.util.NumberUtils;
//import com.exquisapps.billanted.currency.model.enums.Currency;
//import com.exquisapps.billanted.currency.service.ExchangeRateService;
//import com.exquisapps.billanted.notification.service.InAppNotificationService;
//import com.exquisapps.billanted.notification.service.NotificationService;
//import com.exquisapps.billanted.notification.service.PushNotificationService;
//import com.stitches.wallet.exception.InsufficientWalletBalanceException;
//import com.stitches.wallet.exception.InvalidWalletException;
//import com.stitches.wallet.exception.WalletStatusException;
//import com.exquisapps.billanted.wallet.model.dto.*;
//import com.stitches.wallet.model.entity.Wallet;
//import com.stitches.wallet.model.entity.WalletTransactionRequest;
//import com.stitches.wallet.model.enums.TransactionStatus;
//import com.stitches.wallet.model.enums.TransactionType;
//import com.stitches.wallet.model.enums.WalletStatus;
//import com.stitches.wallet.repository.WalletRepository;
//import com.stitches.wallet.repository.WalletTransactionRequestRepository;
//import com.stitches.wallet.service.BaseWalletService;
//import com.stitches.wallet.service.WalletService;
//import com.stitches.wallet.service.WalletTransactionService;
//import com.stitches.wallet.model.dto.*;
//import org.apache.commons.lang3.StringUtils;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Optional;
//
//import static com.itextpdf.kernel.xmp.impl.ParameterAsserts.assertNotNull;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith({SpringExtension.class})
//@ContextConfiguration(classes = WalletServiceImpl.class)
//class WalletServiceImplTest {
//
//    @MockBean
//    WalletRepository walletRepository;
//    @MockBean
//    WalletTransactionRequestRepository walletTransactionRequestRepository;
//    @MockBean
//    BaseWalletService baseWalletService;
//    @MockBean
//    WalletTransactionService walletTransactionService;
//
//    @MockBean
//    ExchangeRateService exchangeRateService;
//
//    @MockBean
//    NotificationService notificationService;
//
//    @MockBean
//    PushNotificationService pushNotificationService;
//
//    @MockBean
//    InAppNotificationService inAppNotificationService;
//
//    @Autowired
//    WalletService walletService;
//
//    @Test
//    void initiateFunding() {
//
//        String transactionId = NumberUtils.generate(16);
//        String walletId = NumberUtils.generate(10);
//        String customerId = NumberUtils.generate(9);
//
//        Wallet wallet = Wallet.builder()
//                .walletId(walletId)
//                .customerId(customerId)
//                .status(WalletStatus.A)
//                .currency(Currency.NGN)
//                .balance(BigDecimal.valueOf(500))
//                .build();
//
//        WalletTransactionRequest walletTransactionRequest = WalletTransactionRequest.builder()
//                .transactionId(transactionId)
//                .currency(Currency.NGN)
//                .transactionType(TransactionType.C)
//                .status(TransactionStatus.S)
//                .build();
//
//        when(walletTransactionService.generateTransactionId()).thenReturn(transactionId);
//        when(walletRepository.findByWalletId(walletId)).thenReturn(Optional.of(wallet));
//        when(walletTransactionRequestRepository.saveAndFlush(any(WalletTransactionRequest.class))).thenReturn(walletTransactionRequest);
//
//        TransactionRequest transactionRequest = TransactionRequest.builder()
//                .customerId(customerId)
//                .walletId(walletId)
//                .amount(BigDecimal.valueOf(200))
//                .currency("NGN")
//                .build();
//
//        WalletTransactionRequestDto walletTransactionRequestDto = walletService.initiateFunding(transactionRequest);
//        assertNotNull(walletTransactionRequestDto);
//        assertEquals(transactionId, walletTransactionRequestDto.getTransactionId());
//
//        verify(walletTransactionRequestRepository, times(1)).saveAndFlush(any(WalletTransactionRequest.class));
//
//    }
//
//
//    @Test
//    void throwException_whenWalletNotMatchingCustomerWallet() {
//
//        String walletId = NumberUtils.generate(10);
//        String customerId = NumberUtils.generate(9);
//
//        Wallet wallet = Wallet.builder()
//                .walletId(walletId)
//                .customerId(customerId)
//                .status(WalletStatus.A)
//                .currency(Currency.NGN)
//                .balance(BigDecimal.valueOf(500))
//                .build();
//
//        when(walletRepository.findByWalletId(walletId)).thenReturn(Optional.of(wallet));
//
//        TransactionRequest transactionRequest = TransactionRequest.builder()
//                .customerId("12345678")
//                .walletId(walletId)
//                .amount(BigDecimal.valueOf(200))
//                .currency("NGN")
//                .build();
//
//        assertThrows(InvalidWalletException.class, () -> {
//           walletService.initiateFunding(transactionRequest);
//        });
//    }
//
//    @Test
//    void throwException_whenWalletNotActiveDuringTransaction() {
//
//        String walletId = NumberUtils.generate(10);
//        String customerId = NumberUtils.generate(9);
//
//        Wallet wallet = Wallet.builder()
//                .walletId(walletId)
//                .customerId(customerId)
//                .status(WalletStatus.S)
//                .currency(Currency.NGN)
//                .balance(BigDecimal.valueOf(500))
//                .build();
//
//        when(walletRepository.findByWalletId(walletId)).thenReturn(Optional.of(wallet));
//
//        TransactionRequest transactionRequest = TransactionRequest.builder()
//                .customerId(customerId)
//                .walletId(walletId)
//                .amount(BigDecimal.valueOf(200))
//                .currency("NGN")
//                .build();
//
//        assertThrows(WalletStatusException.class, () -> {
//           walletService.initiateFunding(transactionRequest);
//        });
//    }
//
//
//    @Test
//    void throwException_whenWalletFundingAmountIsNull() {
//
//        String walletId = NumberUtils.generate(10);
//        String customerId = NumberUtils.generate(9);
//
//        Wallet wallet = Wallet.builder()
//                .walletId(walletId)
//                .customerId(customerId)
//                .status(WalletStatus.S)
//                .currency(Currency.NGN)
//                .balance(BigDecimal.valueOf(500))
//                .build();
//
//        when(walletRepository.findByWalletId(walletId)).thenReturn(Optional.of(wallet));
//
//        TransactionRequest transactionRequest = TransactionRequest.builder()
//                .customerId(customerId)
//                .walletId(walletId)
//                .currency("NGN")
//                .build();
//
//        InvalidWalletException invalidWalletException = assertThrows(InvalidWalletException.class, () -> {
//            walletService.initiateFunding(transactionRequest);
//        });
//        assertEquals("Invalid funding amount: null", invalidWalletException.getMessage());
//    }
//
//    @Test
//    void throwException_whenWalletFundingAmountIsNegative() {
//
//        String walletId = NumberUtils.generate(10);
//        String customerId = NumberUtils.generate(9);
//
//        Wallet wallet = Wallet.builder()
//                .walletId(walletId)
//                .customerId(customerId)
//                .status(WalletStatus.S)
//                .currency(Currency.NGN)
//                .balance(BigDecimal.valueOf(500))
//                .build();
//
//        when(walletRepository.findByWalletId(walletId)).thenReturn(Optional.of(wallet));
//
//        TransactionRequest transactionRequest = TransactionRequest.builder()
//                .customerId(customerId)
//                .walletId(walletId)
//                .currency("NGN")
//                .amount(BigDecimal.valueOf(-200))
//                .build();
//
//        InvalidWalletException invalidWalletException = assertThrows(InvalidWalletException.class, () -> {
//            walletService.initiateFunding(transactionRequest);
//        });
//        assertEquals("Invalid funding amount: -200", invalidWalletException.getMessage());
//    }
//
//     @Test
//    void throwException_whenWalletFundingAmountIsZero() {
//
//        String walletId = NumberUtils.generate(10);
//        String customerId = NumberUtils.generate(9);
//
//        Wallet wallet = Wallet.builder()
//                .walletId(walletId)
//                .customerId(customerId)
//                .status(WalletStatus.S)
//                .currency(Currency.NGN)
//                .balance(BigDecimal.valueOf(500))
//                .build();
//
//        when(walletRepository.findByWalletId(walletId)).thenReturn(Optional.of(wallet));
//
//        TransactionRequest transactionRequest = TransactionRequest.builder()
//                .customerId(customerId)
//                .walletId(walletId)
//                .currency("NGN")
//                .amount(BigDecimal.ZERO)
//                .build();
//
//        InvalidWalletException invalidWalletException = assertThrows(InvalidWalletException.class, () -> {
//            walletService.initiateFunding(transactionRequest);
//        });
//        assertEquals("Invalid funding amount: 0", invalidWalletException.getMessage());
//    }
//
//    @Test
//    void cancelFunding() {
//
//        String transactionId = NumberUtils.generate(16);
//        WalletTransactionRequestDto transactionRequestDto = walletService.cancelFunding(transactionId);
//
//        assertNotNull(transactionRequestDto);
//        assertEquals(transactionId, transactionRequestDto.getTransactionId());
//
//        verify(walletTransactionService, times(1)).updateTransactionStatus(transactionId, TransactionStatus.C);
//    }
//
//    @Test
//    void createWallet() {
//
//        String customerId = NumberUtils.generate(9);
//        String walletId = NumberUtils.generate(10);
//
//        WalletRequest walletRequest = WalletRequest.builder()
//                .customerId(customerId)
//                .currency("NGN")
//                .build();
//
//        Wallet newWallet = Wallet.builder()
//                .walletId(walletId)
//                .customerId(walletRequest.getCustomerId())
//                .currency(Currency.valueOf(walletRequest.getCurrency()))
//                .balance(BigDecimal.ZERO)
//                .name(!StringUtils.isBlank(walletRequest.getName()) ? walletRequest.getName() : "Wallet" + walletId.substring(6))
//                .status(WalletStatus.A)
//                .build();
//
//        when(walletRepository.saveAndFlush(any(Wallet.class))).thenReturn(newWallet);
//
//        WalletDto walletDto = walletService.createWallet(walletRequest);
//
//        assertNotNull(walletDto);
//        assertEquals(walletId, walletDto.getWalletId());
//        assertEquals(BigDecimal.ZERO, walletDto.getBalance());
//        verify(walletRepository, times(1)).saveAndFlush(any(Wallet.class));
//
//    }
//
//    @Test
//    void creditWallet() {
//
//        String walletId = NumberUtils.generate(10);
//        String customerId = NumberUtils.generate(9);
//
//        Wallet wallet = Wallet.builder()
//                .walletId(walletId)
//                .customerId(customerId)
//                .status(WalletStatus.A)
//                .currency(Currency.NGN)
//                .balance(BigDecimal.valueOf(500))
//                .build();
//
//        WalletCreditRequest creditRequest = WalletCreditRequest.builder()
//                .walletId(walletId)
//                .customerId(customerId)
//                .amount(BigDecimal.valueOf(500))
//                .currency(Currency.NGN)
//                .transactionStatus(TransactionStatus.S)
//                .build();
//
//        Wallet updatedWallet = Wallet.builder()
//                .walletId(walletId)
//                .customerId(wallet.getCustomerId())
//                .status(wallet.getStatus())
//                .currency(wallet.getCurrency())
//                .balance(wallet.getBalance().add(BigDecimal.valueOf(500)))
//                .build();
//
//        WalletTransactionDto walletTransactionDto = WalletTransactionDto.builder()
//                .amount(creditRequest.getAmount())
//                .status(TransactionStatus.S)
//                .transactionType(TransactionType.D)
//                .build();
//
//        when(walletRepository.findByWalletId(walletId)).thenReturn(Optional.of(wallet));
//        when(walletRepository.saveAndFlush(wallet)).thenReturn(updatedWallet);
//        when(walletTransactionService.createTransaction(any(Wallet.class), any(WalletTransactionDto.class))).thenReturn(walletTransactionDto);
//
//        WalletTransactionDto transactionDto = walletService.creditCustomerWallet(creditRequest);
//
//        assertNotNull(transactionDto);
//        assertEquals(BigDecimal.valueOf(500), transactionDto.getAmount());
//
//        verify(walletRepository, times(1)).saveAndFlush(wallet);
//        verify(walletTransactionService, times(1)).createTransaction(any(Wallet.class), any(WalletTransactionDto.class));
//        verify(baseWalletService, times(1)).creditInflowBaseWallet(any(BaseWalletCreditRequest.class));
//
//    }
//
//    @Test
//    void debitWallet() {
//
//        String walletId = NumberUtils.generate(10);
//        String customerId = NumberUtils.generate(9);
//
//        Wallet wallet = Wallet.builder()
//                .walletId(walletId)
//                .customerId(customerId)
//                .status(WalletStatus.A)
//                .currency(Currency.NGN)
//                .balance(BigDecimal.valueOf(1000))
//                .build();
//
//        WalletDebitRequest debitRequest = WalletDebitRequest.builder()
//                .walletId(walletId)
//                .customerId(customerId)
//                .amount(BigDecimal.valueOf(500))
//                .currency(Currency.NGN)
//                .transactionStatus(TransactionStatus.S)
//                .build();
//
//        Wallet updatedWallet = Wallet.builder()
//                .walletId(walletId)
//                .customerId(wallet.getCustomerId())
//                .status(wallet.getStatus())
//                .currency(wallet.getCurrency())
//                .balance(wallet.getBalance().subtract(BigDecimal.valueOf(500)))
//                .build();
//
//        WalletTransactionDto walletTransactionDto = WalletTransactionDto.builder()
//                .amount(debitRequest.getAmount())
//                .status(TransactionStatus.S)
//                .transactionType(TransactionType.D)
//                .build();
//
//        when(walletRepository.findByWalletId(walletId)).thenReturn(Optional.of(wallet));
//        when(walletRepository.saveAndFlush(wallet)).thenReturn(updatedWallet);
//        when(walletTransactionService.createTransaction(any(Wallet.class), any(WalletTransactionDto.class))).thenReturn(walletTransactionDto);
//
//
//        WalletTransactionDto transactionDto = walletService.debitCustomerWallet(debitRequest);
//
//        assertNotNull(transactionDto);
//        assertEquals(BigDecimal.valueOf(500), transactionDto.getAmount());
//
//        verify(walletRepository, times(1)).saveAndFlush(wallet);
//        verify(walletTransactionService, times(1)).createTransaction(any(Wallet.class), any(WalletTransactionDto.class));
//        verify(baseWalletService, times(1)).debitInflowBaseWallet(any(BaseWalletDebitRequest.class));
//
//    }
//
//
//    @Test
//    void throwExceptionForInsufficientBalance_whenDebitWallet() {
//
//        String walletId = NumberUtils.generate(10);
//        String customerId = NumberUtils.generate(9);
//
//        Wallet wallet = Wallet.builder()
//                .walletId(walletId)
//                .customerId(customerId)
//                .status(WalletStatus.A)
//                .currency(Currency.NGN)
//                .balance(BigDecimal.valueOf(1000))
//                .build();
//
//        WalletDebitRequest debitRequest = WalletDebitRequest.builder()
//                .walletId(walletId)
//                .customerId(customerId)
//                .amount(BigDecimal.valueOf(1200))
//                .currency(Currency.NGN)
//                .transactionStatus(TransactionStatus.S)
//                .build();
//
//
//        when(walletRepository.findByWalletId(walletId)).thenReturn(Optional.of(wallet));
//
//        InsufficientWalletBalanceException exception = assertThrows(InsufficientWalletBalanceException.class, () -> {
//             walletService.debitCustomerWallet(debitRequest);
//        });
//
//        assertEquals(ResponseStatus.INSUFFICIENT_WALLET_BALANCE.getMessage(), exception.getMessage());
//    }
//
//    @Test
//    void getAllWallets() {
//
//        String customerId = NumberUtils.generate(9);
//        String walletId = NumberUtils.generate(10);
//
//        Wallet wallet = Wallet.builder()
//                .walletId(walletId)
//                .customerId(customerId)
//                .status(WalletStatus.A)
//                .currency(Currency.NGN)
//                .balance(BigDecimal.valueOf(1000))
//                .build();
//
//        List<Wallet> wallets = List.of(wallet);
//
//        when(walletRepository.findByCustomerId(customerId)).thenReturn(wallets);
//
//        List<WalletDto> walletDtos = walletService.getAllWallets(customerId);
//        assertEquals(1, walletDtos.size());
//    }
//
//
//    @Test
//    void processFundingForSuccessfulTransaction() {
//
//        String transactionId = NumberUtils.generate(16);
//        String walletId = NumberUtils.generate(10);
//        String customerId = NumberUtils.generate(9);
//
//        TransactionRequest transactionRequest = TransactionRequest.builder()
//                .transactionId(transactionId)
//                .customerId(customerId)
//                .amount(new BigDecimal("0.00"))
//                .walletId(walletId)
//                .status(TransactionStatus.S)
//                .txRef("123567")
//                .build();
//
//        Wallet wallet = Wallet.builder()
//                .walletId(walletId)
//                .customerId(customerId)
//                .status(WalletStatus.A)
//                .currency(Currency.NGN)
//                .balance(BigDecimal.valueOf(1000))
//                .build();
//
//        WalletTransactionRequest walletTransactionRequest = WalletTransactionRequest.builder()
//                .transactionId(transactionId)
//                .walletId(walletId)
//                .customerId(customerId)
//                .currency(Currency.NGN)
//                .amount(BigDecimal.valueOf(200))
//                .status(TransactionStatus.S)
//                .transactionType(TransactionType.C)
//                .build();
//
//        WalletTransactionDto transactionDto = WalletTransactionDto.builder()
//                .amount(transactionRequest.getAmount())
//                .balance(BigDecimal.valueOf(1000))
//                .transactionType(TransactionType.C)
//                .status(walletTransactionRequest.getStatus())
//                .description(transactionRequest.getDescription())
//                .paymentTransactionId(transactionRequest.getTxRef())
//                .build();
//
//        when(walletRepository.findByWalletId(walletId)).thenReturn(Optional.of(wallet));
//        when(walletTransactionService.updateTransactionRequest(transactionRequest)).thenReturn(walletTransactionRequest);
//        when(walletTransactionService.createTransaction(any(Wallet.class), any(WalletTransactionDto.class))).thenReturn(transactionDto);
//        when(walletRepository.saveAndFlush(any(Wallet.class))).thenReturn(wallet);
//
//        WalletTransactionRequestDto transactionRequestDto = walletService.processFunding(transactionRequest);
//        assertNotNull(transactionRequestDto);
//        assertEquals(TransactionStatus.S.getDescription(), transactionRequestDto.getStatus());
//
//        verify(walletTransactionService, times(1)).createTransaction(any(Wallet.class), any(WalletTransactionDto.class));
//
//    }
//
//
//
//    @Test
//    void processFundingForFailedTransaction() {
//
//        String transactionId = NumberUtils.generate(16);
//        String walletId = NumberUtils.generate(10);
//        String customerId = NumberUtils.generate(9);
//
//        TransactionRequest transactionRequest = TransactionRequest.builder()
//                .transactionId(transactionId)
//                .customerId(customerId)
//                .walletId(walletId)
//                .status(TransactionStatus.F)
//                .txRef("123567")
//                .build();
//
//        Wallet wallet = Wallet.builder()
//                .walletId(walletId)
//                .customerId(customerId)
//                .status(WalletStatus.A)
//                .currency(Currency.NGN)
//                .balance(BigDecimal.valueOf(1000))
//                .build();
//
//        WalletTransactionRequest walletTransactionRequest = WalletTransactionRequest.builder()
//                .transactionId(transactionId)
//                .walletId(walletId)
//                .customerId(customerId)
//                .currency(Currency.NGN)
//                .amount(BigDecimal.valueOf(200))
//                .status(TransactionStatus.F)
//                .transactionType(TransactionType.C)
//                .build();
//
//        WalletTransactionDto transactionDto = WalletTransactionDto.builder()
//                .amount(transactionRequest.getAmount())
//                .transactionType(TransactionType.C)
//                .status(walletTransactionRequest.getStatus())
//                .description(transactionRequest.getDescription())
//                .paymentTransactionId(transactionRequest.getTxRef())
//                .build();
//
//        when(walletRepository.findByWalletId(walletId)).thenReturn(Optional.of(wallet));
//        when(walletTransactionService.updateTransactionRequest(transactionRequest)).thenReturn(walletTransactionRequest);
//        when(walletTransactionService.createTransaction(any(Wallet.class), any(WalletTransactionDto.class))).thenReturn(transactionDto);
//        when(walletRepository.saveAndFlush(any(Wallet.class))).thenReturn(wallet);
//
//        WalletTransactionRequestDto transactionRequestDto = walletService.processFunding(transactionRequest);
//        assertNotNull(transactionRequestDto);
//        assertEquals(TransactionStatus.F.getDescription(), transactionRequestDto.getStatus());
//
//        verify(walletTransactionService, times(1)).createTransaction(any(Wallet.class), any(WalletTransactionDto.class));
//
//    }
//}
