//package com.stitch.payment.service.impl;
//
//
//import com.exquisapps.billanted.currency.service.ExchangeRateService;
//import com.stitch.payment.repository.PaymentReversalRequestRepository;
//import com.stitch.payment.service.PaymentCardService;
//import com.stitch.payment.service.TransactionService;
//import com.exquisapps.billanted.psp.service.ForeignPaymentProviderService;
//import com.exquisapps.billanted.psp.service.LocalPaymentProviderService;
//import com.exquisapps.billanted.wallet.model.dto.WalletDebitRequest;
//import com.exquisapps.billanted.wallet.model.dto.WalletTransactionDto;
//import com.exquisapps.billanted.wallet.service.WalletService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class PaymentServiceImplTest {
//
//    @Mock
//    private LocalPaymentProviderService mockLocalPaymentProviderService;
//
//    @Mock
//    private ForeignPaymentProviderService foreignPaymentProviderService;
//    @Mock
//    private WalletService mockWalletService;
//    @Mock
//    private PaymentCardService paymentCardService;
//
//    @Mock
//    private TransactionService transactionService;
//
//    @Mock
//    private ExchangeRateService exchangeRateService;
//
//    @Mock
//    private PaymentReversalRequestRepository reversalRequestRepository;
//
//    private PaymentServiceImpl paymentServiceImplUnderTest;
//
//    @BeforeEach
//    void setUp() {
//        paymentServiceImplUnderTest = new PaymentServiceImpl(mockLocalPaymentProviderService, foreignPaymentProviderService, mockWalletService,
//            paymentCardService, transactionService, exchangeRateService, reversalRequestRepository);
//    }
//
////    @Test
////    void testVerifyTransaction() {
////        final VerificationRequest verificationRequest = new VerificationRequest();
////        verificationRequest.setTransactionId("transactionId");
////        verificationRequest.setCurrency(Currency.NGN.name());
////        verificationRequest.setAmount(new BigDecimal("0.00"));
////
////        final VerificationResponse expectedResult = new VerificationResponse();
////        expectedResult.setStatus(VerificationStatus.SUCCESSFUL);
////        expectedResult.setTransactionId("transactionId");
////        expectedResult.setLast4digits("1234");
////        expectedResult.setFirst6digits("1234");
////        expectedResult.setType("Visa");
////        expectedResult.setToken("token");
////
////        final VerificationResponse verificationResponse = new VerificationResponse();
////        verificationResponse.setStatus(VerificationStatus.SUCCESSFUL);
////        verificationResponse.setTransactionId("transactionId");
////        verificationResponse.setLast4digits("1234");
////        verificationResponse.setFirst6digits("1234");
////        verificationResponse.setType("Visa");
////        verificationResponse.setToken("token");
////        final VerificationRequest verificationRequest1 = new VerificationRequest();
////        verificationRequest1.setTransactionId("transactionId");
////        verificationRequest1.setCurrency(Currency.NGN.name());
////        verificationRequest1.setAmount(new BigDecimal("0.00"));
////        when(mockLocalPaymentProviderService.verifyTransaction(verificationRequest1)).thenReturn(verificationResponse);
////        when(mockPaymentCardRepository.existsByFirst6digitsAndLast4digitsAndCustomerId("1234", "1234", "customerId")).thenReturn(false);
////
////        final VerificationResponse result = paymentServiceImplUnderTest.verifyTransaction("customerId", true,
////                verificationRequest);
////
////        assertThat(result).isEqualTo(expectedResult);
////
////        final PaymentCard entity = new PaymentCard();
////        entity.setCustomerId("customerId");
////        entity.setLast4digits("1234");
////        entity.setCardType(CardType.VISA);
////        entity.setToken("token");
////        entity.setProvider(Provider.FLUTTERWAVE);
////        entity.setComments("Debit card");
////        verify(mockPaymentCardRepository).saveAndFlush(entity);
////    }
//
////    @Test
////    void testVerifyTransactionCardAlreadyExist() {
////        final VerificationRequest verificationRequest = new VerificationRequest();
////        verificationRequest.setTransactionId("transactionId");
////        verificationRequest.setCurrency(Currency.NGN.name());
////        verificationRequest.setAmount(new BigDecimal("0.00"));
////
////        final VerificationResponse expectedResult = new VerificationResponse();
////        expectedResult.setStatus(VerificationStatus.SUCCESSFUL);
////        expectedResult.setTransactionId("transactionId");
////        expectedResult.setLast4digits("1234");
////        expectedResult.setFirst6digits("1234");
////        expectedResult.setType("Visa");
////        expectedResult.setToken("token");
////
////        final VerificationResponse verificationResponse = new VerificationResponse();
////        verificationResponse.setStatus(VerificationStatus.SUCCESSFUL);
////        verificationResponse.setTransactionId("transactionId");
////        verificationResponse.setLast4digits("1234");
////        verificationResponse.setFirst6digits("1234");
////        verificationResponse.setType("Visa");
////        verificationResponse.setToken("token");
////        final VerificationRequest verificationRequest1 = new VerificationRequest();
////        verificationRequest1.setTransactionId("transactionId");
////        verificationRequest1.setCurrency(Currency.NGN.name());
////        verificationRequest1.setAmount(new BigDecimal("0.00"));
////        when(mockLocalPaymentProviderService.verifyTransaction(verificationRequest1)).thenReturn(verificationResponse);
////        when(mockPaymentCardRepository.existsByFirst6digitsAndLast4digitsAndCustomerId("1234", "1234", "customerId")).thenReturn(true);
////
////        final VerificationResponse result = paymentServiceImplUnderTest.verifyTransaction("customerId", true,
////            verificationRequest);
////
////        assertThat(result).isEqualTo(expectedResult);
////    }
//
////    @Test
////    void testVerifyTransactionReturnedFailed() {
////        final VerificationRequest verificationRequest = new VerificationRequest();
////        verificationRequest.setTransactionId("transactionId");
////        verificationRequest.setCurrency(Currency.NGN.name());
////        verificationRequest.setAmount(new BigDecimal("0.00"));
////
////        final VerificationResponse expectedResult = new VerificationResponse();
////        expectedResult.setStatus(VerificationStatus.FAILED);
////        expectedResult.setTransactionId("transactionId");
////        expectedResult.setLast4digits("1234");
////        expectedResult.setType("Visa");
////        expectedResult.setToken("token");
////
////        final VerificationResponse verificationResponse = new VerificationResponse();
////        verificationResponse.setStatus(VerificationStatus.FAILED);
////        verificationResponse.setTransactionId("transactionId");
////        verificationResponse.setLast4digits("1234");
////        verificationResponse.setType("Visa");
////        verificationResponse.setToken("token");
////        final VerificationRequest verificationRequest1 = new VerificationRequest();
////        verificationRequest1.setTransactionId("transactionId");
////        verificationRequest1.setCurrency(Currency.NGN.name());
////        verificationRequest1.setAmount(new BigDecimal("0.00"));
////        when(mockLocalPaymentProviderService.verifyTransaction(verificationRequest1)).thenReturn(verificationResponse);
////
////        final VerificationResponse result = paymentServiceImplUnderTest.verifyTransaction("customerId", true,
////            verificationRequest);
////
////        assertThat(result).isEqualTo(expectedResult);
////    }
//
//    @Test
//    void testDebitWallet() {
//        final WalletDebitRequest walletDebitRequest = WalletDebitRequest.builder().build();
//        WalletTransactionDto transactionDto = WalletTransactionDto.builder()
//            .transactionId("transactionId")
//            .build();
//        when(mockWalletService.debitCustomerWallet(WalletDebitRequest.builder().build())).thenReturn(transactionDto);
//        paymentServiceImplUnderTest.debitWallet(walletDebitRequest);
//    }
//}
