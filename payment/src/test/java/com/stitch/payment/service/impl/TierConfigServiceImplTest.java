//package com.exquisapps.billanted.payment.service.impl;
//
//import com.exquisapps.billanted.currency.model.dto.Exchange;
//import com.exquisapps.billanted.currency.model.enums.Currency;
//import com.exquisapps.billanted.currency.service.impl.ExchangeRateServiceImpl;
//import com.exquisapps.billanted.payment.model.dto.DailyLimitRequest;
//import com.exquisapps.billanted.payment.model.dto.TierConfigDto;
//import com.exquisapps.billanted.payment.model.entity.TierConfig;
//import com.exquisapps.billanted.payment.model.entity.Transaction;
//import com.exquisapps.billanted.payment.model.enums.TierActionType;
//import com.exquisapps.billanted.payment.model.enums.TransactionStatus;
//import com.exquisapps.billanted.payment.model.enums.TransactionType;
//import com.exquisapps.billanted.payment.repository.TierConfigRepository;
//import com.exquisapps.billanted.payment.repository.TransactionRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//import java.math.BigDecimal;
//import java.time.Instant;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//import org.mockito.InjectMocks;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith({MockitoExtension.class})
//class TierConfigServiceImplTest {
//
//    @Mock
//    private TierConfigRepository tierConfigRepository;
//
//    @InjectMocks
//    private TierConfigServiceImpl tierConfigService;
//
//    @Mock
//    private TransactionRepository transactionRepository;
//
//    @InjectMocks
//    private ExchangeRateServiceImpl exchangeRateService;
//
//
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    void setUpTier() {
//        TierConfigDto request = TierConfigDto.builder()
//                .tier("tier")
//                .amount(BigDecimal.TEN)
//                .actionType(TierActionType.F)
//                .currency(Currency.NGN)
//                .build();
//
//        TierConfig tierConfig = new TierConfig();
//        tierConfig.setAmount(BigDecimal.TEN);
//        tierConfig.setCurrency(Currency.NGN);
//        tierConfig.setActionType(TierActionType.F);
//
//        lenient().when(tierConfigRepository.findByTierIgnoreCaseAndActionTypeAndCurrency(request.getTier(), request.getActionType(), request.getCurrency())).thenReturn(Optional.of(tierConfig));
//
//        TierConfigDto actual = tierConfigService.setUpTier(request);
//
//        assertEquals(request, actual);
//
//    }
//
//
//    @Test
//    void fetchTierConfig() {
//
//        TierConfig tierConfig = new TierConfig();
//        tierConfig.setTier("tier");
//        tierConfig.setCurrency(Currency.NGN);
//        tierConfig.setActionType(TierActionType.F);
//
//        lenient().when(tierConfigRepository.findByTierIgnoreCaseAndActionTypeAndCurrency("tier", TierActionType.F, Currency.NGN)).thenReturn(Optional.of(tierConfig));
//
//        BigDecimal actual = tierConfigService.fetchTierConfig("tier", TierActionType.F, Currency.NGN);
//
//        assertEquals(actual, new BigDecimal("10000"));
//    }
//
//    @Test
//    void cumulativeDailyLimits() {
//        // In progress
//
//        DailyLimitRequest dailyLimitRequest = DailyLimitRequest.builder()
//                .tier("tier")
//                .actionType(TierActionType.F)
//                .currency(Currency.NGN)
//                .build();
//        TierConfig tierConfig = new TierConfig();
//        tierConfig.setId(1L);
//
//        Transaction transaction = new Transaction();
//        transaction.setCustomerId("1");
//        transaction.setTransactionType(TransactionType.PAY_BILL);
//        transaction.setStatus(TransactionStatus.C);
//        transaction.setSrcCurrency(Currency.CAD);
//        transaction.setDateCreated(Instant.MIN);
//
//        List<TransactionStatus> transactionStatusList = List.of(TransactionStatus.F, TransactionStatus.P, TransactionStatus.C);
//
//        when(tierConfigRepository.findById(1L)).thenReturn(Optional.of(tierConfig));
//
//        when(transactionRepository.findAllByCustomerIdAndTransactionTypeAndStatusInAndSrcCurrencyAndDateCreatedBetween(
//                transaction.getCustomerId(), transaction.getTransactionType(), transactionStatusList, transaction.getCurrency(), transaction.getDateCreated(), Instant.MAX))
//                .thenReturn(Arrays.asList(transaction));
//
//        when(exchangeRateService.getExchange(Currency.NGN, BigDecimal.ONE))
//                .thenReturn(new Exchange());
//
//        assertDoesNotThrow(() -> tierConfigService.cumulativeDailyLimits(dailyLimitRequest));
//    }
//
//
//    @Test
//    void fetchAllTierConfig() {
//
//        TierConfig tierConfig1 = new TierConfig("tier", BigDecimal.TEN, Currency.NGN, TierActionType.F);
//        TierConfig tierConfig2 = new TierConfig("tier", BigDecimal.ONE, Currency.CAD, TierActionType.W);
//
//        List<TierConfig> tierConfigList = List.of(tierConfig1, tierConfig2);
//
//        when(tierConfigRepository.findAll()).thenReturn(tierConfigList);
//
//        List<TierConfigDto> result = tierConfigService.fetchAllTierConfig();
//
//        assertEquals(2, result.size());
//    }
//
//
//}