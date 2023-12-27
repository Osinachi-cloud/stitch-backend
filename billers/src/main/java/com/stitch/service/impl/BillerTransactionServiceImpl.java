package com.stitch.service.impl;


import com.stitch.exception.StitchApiException;
import com.stitch.model.entity.BillTransaction;
import com.stitch.repository.BillerTransactionRepository;
import com.stitch.service.BillerTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BillerTransactionServiceImpl implements BillerTransactionService {

    private final BillerTransactionRepository billerTransactionRepository;


    public BillerTransactionServiceImpl(BillerTransactionRepository billerTransactionRepository) {
        this.billerTransactionRepository = billerTransactionRepository;
    }


    @Override
    public BillTransaction saveBillTransaction(BillTransaction billTransaction) {
        return this.billerTransactionRepository.saveAndFlush(billTransaction);
    }

    @Override
    public BillTransaction retrieveBillTransactionByOrderId(String orderId) {
        return this.billerTransactionRepository.findByOrderId(orderId)
            .orElseThrow(() -> new StitchApiException("Bill transaction does not exist for orderId: "+orderId));
    }

    @Override
    public BillTransaction retrieveBillTransactionByRequestId(String requestId) {
        return this.billerTransactionRepository.findByRequestId(requestId);
    }
}
