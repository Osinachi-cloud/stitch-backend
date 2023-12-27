package com.stitch.psp.service;

import com.stitch.psp.model.dto.PaymentIntentRequest;
import com.stitch.psp.model.dto.PaymentIntentResponse;
import com.stitch.psp.model.dto.stripe.StripeVerificationResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface StripeApi {


    PaymentIntentResponse createPaymentIntent(PaymentIntentRequest paymentIntentRequest);

    PaymentIntent makeCardPayment(PaymentIntentRequest paymentIntentRequest);

    StripeVerificationResponse verifyPayment(String paymentId) throws StripeException;
}
