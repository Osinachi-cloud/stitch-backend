//package com.stitch.payment.service.impl;
//
//import com.stitch.commons.model.dto.Response;
//import com.stitch.commons.util.AlphaNumericUtils;
//import com.stitch.commons.util.NumberUtils;
//import com.stitch.commons.util.ResponseUtils;
//import com.stitch.payment.exception.PaymentException;
//import com.stitch.payment.model.dto.CardPaymentRequest;
//import com.stitch.payment.model.dto.CardPaymentResponse;
//import com.stitch.payment.model.entity.PaymentCard;
//import com.stitch.payment.model.enums.CardType;
//import com.stitch.payment.repository.PaymentCardRepository;
//import com.stitch.payment.service.PaymentCardService;
//import com.stitch.psp.model.PaymentVerificationResponse;
//import com.stitch.psp.model.dto.flutterwave.CardPayment;
//import com.stitch.psp.model.enums.PaymentProvider;
//import com.stitch.psp.service.ForeignPaymentProviderService;
//import com.stitch.psp.service.LocalPaymentProviderService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@Slf4j
//public class PaymentCardServiceImpl implements PaymentCardService {
//
//    private final PaymentCardRepository paymentCardRepository;
//    private final LocalPaymentProviderService localPaymentProviderService;
//    private final ForeignPaymentProviderService foreignPaymentProviderService;
//
//    public PaymentCardServiceImpl(PaymentCardRepository paymentCardRepository, LocalPaymentProviderService localPaymentProviderService, ForeignPaymentProviderService foreignPaymentProviderService) {
//        this.paymentCardRepository = paymentCardRepository;
//        this.localPaymentProviderService = localPaymentProviderService;
//        this.foreignPaymentProviderService = foreignPaymentProviderService;
//    }
//
//
//    @Override
//    public void saveCard(String customerId, PaymentVerificationResponse paymentVerificationResponse) {
//        log.debug("Saving card details: {}", paymentVerificationResponse);
//        String token = paymentVerificationResponse.getToken();
//        String last4digits = paymentVerificationResponse.getLast4digits();
//        String first6digits = paymentVerificationResponse.getFirst6digits();
//        String expiry = paymentVerificationResponse.getExpiry();
//
//        PaymentCard paymentCard = new PaymentCard();
//        paymentCard.setCardId(AlphaNumericUtils.generate(12));
//        paymentCard.setCustomerId(customerId);
//        paymentCard.setCardType(CardType.fromName(paymentVerificationResponse.getType()));
//        paymentCard.setToken(token);
//        paymentCard.setLast4digits(last4digits);
//        paymentCard.setFirst6digits(first6digits);
//        paymentCard.setExpiry(expiry);
//        paymentCard.setFingerprint(paymentVerificationResponse.getCardFingerprint());
//        paymentCard.setPaymentProvider(paymentVerificationResponse.getPaymentProvider());
//
//        if (PaymentProvider.FLUTTERWAVE.equals(paymentVerificationResponse.getPaymentProvider())) {
//            if (!paymentCardRepository.existsByFirst6digitsAndLast4digitsAndCustomerId(first6digits, last4digits, customerId)) {
//                paymentCardRepository.saveAndFlush(paymentCard);
//            }
//        } else if (PaymentProvider.STRIPE.equals(paymentVerificationResponse.getPaymentProvider())) {
//            if (!paymentCardRepository.existsByFingerprintAndCustomerId(paymentVerificationResponse.getCardFingerprint(), customerId)) {
//                paymentCardRepository.saveAndFlush(paymentCard);
//            }
//        }
//    }
//
//    public PaymentVerificationResponse chargeCard(CardPaymentRequest cardPaymentRequest) {
//
//        PaymentCard paymentCard = paymentCardRepository.findByCardId(cardPaymentRequest.getCardId())
//                .orElseThrow(() -> new PaymentException("Card not found"));
//
//        if (!paymentCard.getCustomerId().equals(cardPaymentRequest.getCustomerId())) {
//            throw new PaymentException("Not user card");
//        }
//        String reference = NumberUtils.generate(16);
//        PaymentVerificationResponse paymentVerificationResponse = null;
//
//        CardPayment cardPayment = CardPayment.builder()
//                .amount(cardPaymentRequest.getAmount())
//                .srcCurrency(cardPaymentRequest.getSrcCurrency())
//                .destCurrency(cardPaymentRequest.getDestCurrency())
//                .email(cardPaymentRequest.getEmail())
//                .customerId(cardPaymentRequest.getCustomerId())
//                .token(paymentCard.getToken())
//                .narration(cardPaymentRequest.getNarration())
//                .transactionReference(reference)
//                .build();
//
//        if (PaymentProvider.FLUTTERWAVE.equals(paymentCard.getPaymentProvider())) {
//            cardPayment.setCountry("NG");
//            paymentVerificationResponse = localPaymentProviderService.chargeCard(cardPayment);
//
//        } else if (PaymentProvider.STRIPE.equals(paymentCard.getPaymentProvider())) {
//            paymentVerificationResponse = foreignPaymentProviderService.chargeCard(cardPayment);
//        }
//        return paymentVerificationResponse;
//    }
//
//    public List<CardPaymentResponse> getCustomerCards(String customerId) {
//        return paymentCardRepository.findAllByCustomerId(customerId).stream()
//                .map(this::mapEntityToDto).collect(Collectors.toList());
//    }
//
//    private CardPaymentResponse mapEntityToDto(PaymentCard paymentCard) {
//        return CardPaymentResponse.builder()
//                .cardId(paymentCard.getCardId())
//                .last4digits(paymentCard.getLast4digits())
//                .first6digits(paymentCard.getFirst6digits())
//                .expiry(paymentCard.getExpiry())
//                .cardType(paymentCard.getCardType())
//                .build();
//    }
//
//    @Override
//    @Transactional
//    public Response deleteCard(String customerId, String cardId) {
//        PaymentCard paymentCard = paymentCardRepository.findByCardId(cardId)
//            .orElseThrow(() -> new PaymentException("Card not found"));
//
//        if(!paymentCard.getCustomerId().equals(customerId)){
//            throw new PaymentException("Not user card");
//        }
//        paymentCardRepository.deleteByCardId(cardId);
//        return ResponseUtils.createDefaultSuccessResponse();
//    }
//}
