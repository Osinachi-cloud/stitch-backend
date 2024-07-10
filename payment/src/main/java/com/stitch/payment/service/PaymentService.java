package com.stitch.payment.service;

import com.stitch.commons.model.dto.Response;
import com.stitch.currency.model.enums.Currency;
import com.stitch.model.entity.ProductOrder;
import com.stitch.payment.model.entity.InitializeTransactionRequest;
import com.stitch.payment.model.entity.InitializeTransactionResponse;
import com.stitch.payment.model.entity.PaymentReversalRequest;
//import com.stitch.payment.model.entity.Transaction;
import com.stitch.payment.model.enums.TransactionStatus;
import com.stitch.payment.model.dto.*;
//import com.stitch.psp.model.PaymentVerificationRequest;
//import com.stitch.psp.model.PaymentVerificationResponse;
//import com.stitch.psp.model.dto.PaymentIntentRequest;
//import com.stitch.psp.model.dto.PaymentIntentResponse;
import com.stitch.wallet.model.dto.WalletDebitRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//public interface PaymentService {
//
//    PaymentVerificationResponse verifyFundingTransaction(String customerId, boolean saveCard, PaymentVerificationRequest paymentVerificationRequest);
//
//    PaymentVerificationResponse verifyOrderTransaction(String customerId, boolean saveCard, PaymentVerificationRequest paymentVerificationRequest);
//
//    PaymentResponse debitWallet(WalletDebitRequest walletDebitRequest);
//    @Async
//    void initiatePaymentReversal(PaymentReversalRequest paymentReversalRequest);
//
//    PaymentVerificationResponse chargeCard(CardPaymentRequest cardPaymentRequest);
//
//    PaymentVerificationResponse chargeCard(CardPaymentRequest cardPaymentRequest, boolean saveCard);
//
//    List<CardPaymentResponse> getCustomerCards(String customerId);
//
//    Transaction addTransaction(TransactionRequest transactionRequest);
//
//    void updateTransactionStatus(String transactionId, TransactionStatus transactionStatus);
//    void updateTransaction(Transaction transaction);
//
//    PaymentIntentResponse createPaymentIntent(PaymentIntentRequest paymentIntentRequest);
//
//    Currency getCustomerCurrency(String customerId);
//
//    PaymentResponse initiateForeignPayment(PaymentRequest paymentRequest);
//
//    Response deleteCard(String customerId, String cardId);
//
//    TransactionDetailsDto fetchPaymentDetails(String transactionId, String customerId);
//
//    Transaction retrieveTransactionByOrderId(String orderId);
//}



public interface PaymentService {

     void initializePayment(ProductOrder order);

     InitializeTransactionResponse initTransaction(InitializeTransactionRequest request) throws Exception;


     PaymentVerificationResponse paymentVerification(String reference) throws Exception;
}