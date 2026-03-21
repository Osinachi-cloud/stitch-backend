package com.stitch.payment.service;

import com.stitch.payment.model.dto.PaymentVerificationResponse;
import com.stitch.payment.model.entity.InitializeTransactionRequest;
import com.stitch.payment.model.entity.InitializeTransactionResponse;

public interface PaymentService {

     InitializeTransactionResponse initTransaction(InitializeTransactionRequest request);
     PaymentVerificationResponse paymentVerification(String reference);
}