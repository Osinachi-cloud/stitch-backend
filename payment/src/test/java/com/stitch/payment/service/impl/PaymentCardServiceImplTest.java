//package com.stitch.payment.service.impl;
//
//import com.stitch.payment.exception.PaymentException;
//import com.stitch.payment.model.dto.CardPaymentRequest;
//import com.stitch.payment.model.dto.CardPaymentResponse;
//import com.stitch.payment.model.entity.PaymentCard;
//import com.stitch.payment.model.enums.CardType;
//import com.stitch.payment.repository.PaymentCardRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.math.BigDecimal;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class PaymentCardServiceImplTest {
//
//    @Mock
//    private PaymentCardRepository mockPaymentCardRepository;
//    @Mock
//    private LocalPaymentProviderService mockPaymentProviderService;
//
//    @Mock
//    private ForeignPaymentProviderService foreignPaymentProviderService;
//
//    private PaymentCardServiceImpl paymentCardServiceImplUnderTest;
//
//    @BeforeEach
//    void setUp() {
//        paymentCardServiceImplUnderTest = new PaymentCardServiceImpl(mockPaymentCardRepository,
//                mockPaymentProviderService, foreignPaymentProviderService);
//    }
//
//    //    @Test
//    void testChargeCard() {
//
//        final CardPaymentRequest cardPaymentRequest = CardPaymentRequest.builder()
//            .cardId("cardId")
//            .amount(new BigDecimal("0.00"))
//            .narration("narration")
//            .customerId("12345671")
//            .email("email@test.com").build();
//
//        final Response expectedResult = new Response(0, "message");
//
//        final PaymentCard paymentCard1 = new PaymentCard();
//        paymentCard1.setId(1L);
//        paymentCard1.setCustomerId("12345678");
//        paymentCard1.setLast4digits("1234");
//        paymentCard1.setCardType(CardType.VISA);
//        paymentCard1.setToken("token");
//        final Optional<PaymentCard> paymentCard = Optional.of(paymentCard1);
//        when(mockPaymentCardRepository.findById(1L)).thenReturn(paymentCard);
//
//
//        final PaymentVerificationResponse verificationResponse = new PaymentVerificationResponse();
//        verificationResponse.setStatus(VerificationStatus.SUCCESSFUL);
//        verificationResponse.setTransactionRefId("transactionId");
//        verificationResponse.setLast4digits("1234");
//        verificationResponse.setCountry("NG");
//        verificationResponse.setType("VISA");
//
////        String reference = NumberUtils.generate(16);
//
//        when(NumberUtils.generate(16)).thenReturn("12345678888886");
//
//        when(mockPaymentProviderService.chargeCard(CardPayment.builder()
//            .email("email@test.com")
//            .token("token")
//            .country("NG")
//            .srcCurrency(Currency.NGN)
//            .amount(new BigDecimal("0.00"))
//            .transactionReference("12345678888886")
//            .narration("narration")
//            .build())).thenReturn(verificationResponse);
//
//        final PaymentVerificationResponse result = paymentCardServiceImplUnderTest.chargeCard(cardPaymentRequest);
//
////        assertThat(result).isEqualTo(expectedResult);
//    }
//
//    @Test
//    void testChargeCard_PaymentCardRepositoryReturnsAbsent() {
//
//        final CardPaymentRequest cardPaymentRequest = CardPaymentRequest.builder()
//            .cardId("cardId")
//            .amount(new BigDecimal("0.00"))
//            .narration("narration")
//            .customerId("12345671")
//            .email("email@test.com").build();
//
//        when(mockPaymentCardRepository.findByCardId("cardId")).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> paymentCardServiceImplUnderTest.chargeCard(cardPaymentRequest))
//            .isInstanceOf(PaymentException.class);
//    }
//
//
//    @Test
//    void testChargeCard_WhenNotCustomerCard() {
//
//        final CardPaymentRequest cardPaymentRequest = CardPaymentRequest.builder()
//            .cardId("cardId")
//            .amount(new BigDecimal("0.00"))
//            .narration("narration")
//            .customerId("12345671")
//            .email("email@test.com").build();
//
//        final PaymentCard paymentCard = new PaymentCard();
//        paymentCard.setId(1L);
//        paymentCard.setCustomerId("12345678");
//        final Optional<PaymentCard> paymentCardOptional = Optional.of(paymentCard);
//
//        when(mockPaymentCardRepository.findByCardId("cardId")).thenReturn(paymentCardOptional);
//
//        assertThatThrownBy(() -> paymentCardServiceImplUnderTest.chargeCard(cardPaymentRequest))
//            .isInstanceOf(PaymentException.class);
//    }
//
//    @Test
//    void testCustomerCards() {
//
//        final List<CardPaymentResponse> expectedResult = List.of(CardPaymentResponse.builder()
//            .cardId("cardId")
//            .last4digits("1234")
//            .cardType(CardType.VISA)
//            .build());
//
//        final PaymentCard paymentCard = new PaymentCard();
//        paymentCard.setId(1L);
//        paymentCard.setCustomerId("12345678");
//        paymentCard.setCardId("cardId");
//        paymentCard.setLast4digits("1234");
//        paymentCard.setCardType(CardType.VISA);
//        paymentCard.setToken("token");
//        final List<PaymentCard> paymentCards = List.of(paymentCard);
//        when(mockPaymentCardRepository.findAllByCustomerId("12345678")).thenReturn(paymentCards);
//        final List<CardPaymentResponse> result = paymentCardServiceImplUnderTest.getCustomerCards("12345678");
//        assertThat(result).isEqualTo(expectedResult);
//    }
//
//    @Test
//    void testCustomerCards_PaymentCardRepositoryReturnsNoItems() {
//        when(mockPaymentCardRepository.findAllByCustomerId("customerId")).thenReturn(Collections.emptyList());
//        final List<CardPaymentResponse> result = paymentCardServiceImplUnderTest.getCustomerCards("customerId");
//        assertThat(result).isEqualTo(Collections.emptyList());
//    }
//
//    @Test
//    void testDeleteCardWhenCardNotFound() {
//        when(mockPaymentCardRepository.findByCardId("cardId")).thenReturn(Optional.empty());
//        assertThatThrownBy(() -> paymentCardServiceImplUnderTest.deleteCard("12345555", "cardId"))
//            .isInstanceOf(PaymentException.class);
//    }
//
//    @Test
//    void testDeleteCardWhenCardNotCustomerOwn() {
//        final PaymentCard paymentCard = new PaymentCard();
//        paymentCard.setId(1L);
//        paymentCard.setCustomerId("12345678");
//        final Optional<PaymentCard> paymentCardOptional = Optional.of(paymentCard);
//
//        when(mockPaymentCardRepository.findByCardId("cardId")).thenReturn(paymentCardOptional);
//
//        assertThatThrownBy(() -> paymentCardServiceImplUnderTest.deleteCard("12345555", "cardId"))
//            .isInstanceOf(PaymentException.class);
//    }
//
//    @Test
//    void testDeleteCardIsSuccessful() {
//        final PaymentCard paymentCard = new PaymentCard();
//        paymentCard.setId(1L);
//        paymentCard.setCustomerId("12345678");
//        final Optional<PaymentCard> paymentCardOptional = Optional.of(paymentCard);
//
//        when(mockPaymentCardRepository.findByCardId("cardId")).thenReturn(paymentCardOptional);
//
//        Response result = paymentCardServiceImplUnderTest.deleteCard("12345678", "cardId");
//
//        assertThat(result.getCode()).isEqualTo(0);
//
//    }
//}
