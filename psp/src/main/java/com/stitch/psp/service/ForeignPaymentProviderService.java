package com.stitch.psp.service;


import com.stitch.psp.model.PaymentVerificationRequest;
import com.stitch.psp.model.PaymentVerificationResponse;
import com.stitch.psp.model.dto.PaymentIntentRequest;
import com.stitch.psp.model.dto.PaymentIntentResponse;
import com.stitch.psp.model.dto.flutterwave.CardPayment;

public interface ForeignPaymentProviderService {


    PaymentIntentResponse createPaymentIntent(PaymentIntentRequest paymentIntentRequest);

    PaymentVerificationResponse verifyTransaction(PaymentVerificationRequest paymentVerificationRequest);

    PaymentVerificationResponse chargeCard(CardPayment request);
}
