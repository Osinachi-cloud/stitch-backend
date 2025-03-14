package com.stitch.payment.service;

import com.stitch.payment.model.entity.Transaction;

public interface TransactionService {
    Transaction saveTransaction(Transaction transaction);
}
