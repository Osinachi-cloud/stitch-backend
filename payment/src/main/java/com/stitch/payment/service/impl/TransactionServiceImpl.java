package com.stitch.payment.service.impl;

import com.stitch.payment.model.entity.Transaction;
import com.stitch.payment.repository.TransactionRepository;
import com.stitch.payment.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    @Override
    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
}
