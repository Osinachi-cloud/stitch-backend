//package com.stitch.payment.service;
//
//import com.stitch.commons.model.dto.Response;
//import com.stitch.payment.model.dto.CardPaymentRequest;
//import com.stitch.payment.model.dto.CardPaymentResponse;
//import com.stitch.psp.model.PaymentVerificationResponse;
//
//import java.util.List;
//
//public interface PaymentCardService {
//
//
//    void saveCard(String customerId, PaymentVerificationResponse paymentVerificationResponse);
//
//    PaymentVerificationResponse chargeCard(CardPaymentRequest cardPaymentRequest);
//
//    List<CardPaymentResponse> getCustomerCards(String customerId);
//
//    Response deleteCard(String customerId, String cardId);
//}
