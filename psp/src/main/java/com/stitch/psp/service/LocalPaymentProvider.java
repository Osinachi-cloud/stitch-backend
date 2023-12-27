package com.stitch.psp.service;


import com.stitch.psp.model.dto.flutterwave.CardPayment;

public interface LocalPaymentProvider {



    Object verifyPayment(String transactionId);

    Object makeCardPayment(CardPayment cardPayment);


}
