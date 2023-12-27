package com.stitch.psp.service;


import com.stitch.psp.model.PaymentVerificationRequest;
import com.stitch.psp.model.PaymentVerificationResponse;
import com.stitch.psp.model.dto.flutterwave.CardPayment;

public interface LocalPaymentProviderService {



    PaymentVerificationResponse verifyTransaction(PaymentVerificationRequest paymentVerificationRequest);

    PaymentVerificationResponse chargeCard(CardPayment cardPayment);
}
