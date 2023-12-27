package com.stitch.payment.service;

import com.stitch.commons.model.dto.PaginatedResponse;
import com.stitch.payment.model.dto.*;
import com.stitch.payment.model.entity.Transaction;
import com.stitch.payment.model.enums.TransactionStatus;

import java.util.List;

public interface TransactionService {

    PaginatedResponse<List<TransactionResponse>> fetchAllCustomerTransactions(String customerId, TransactionHistoryRequest request);

    PaginatedResponse<List<TransactionDto>> fetchCustomerTransactions(String customerId, TransactionHistoryRequest request);

    Transaction createTransaction(TransactionRequest transactionRequest);

    Transaction updateTransactionStatus(String transactionId, TransactionStatus status);

    Transaction updateTransactionStatus(Long id, TransactionStatus status);
    Transaction updateTransaction(Transaction transaction);

    TransactionDetailsDto fetchPaymentDetails(String transactionId, String customerId);

    Transaction retrieveTransaction(String transactionId);

    Transaction retrieveTransactionByOrderId(String orderId);

    PaginatedResponse<List<TransactionDto>> fetchAllTransactions(TransactionHistoryRequest request);
}
