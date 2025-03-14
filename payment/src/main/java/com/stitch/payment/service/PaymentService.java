package com.stitch.payment.service;

import com.stitch.model.entity.ProductOrder;
import com.stitch.payment.model.dto.PaymentVerificationResponse;
import com.stitch.payment.model.entity.InitializeTransactionRequest;
import com.stitch.payment.model.entity.InitializeTransactionResponse;

public interface PaymentService {

     void initializePayment(ProductOrder order);

     InitializeTransactionResponse initTransaction(InitializeTransactionRequest request) throws Exception;


     PaymentVerificationResponse paymentVerification(String reference) throws Exception;
}