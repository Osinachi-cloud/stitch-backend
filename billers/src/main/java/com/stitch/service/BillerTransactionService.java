package com.stitch.service;


import com.stitch.model.entity.BillTransaction;


public interface BillerTransactionService {


    BillTransaction saveBillTransaction(BillTransaction billTransaction);

    BillTransaction retrieveBillTransactionByOrderId(String orderId);

    BillTransaction retrieveBillTransactionByRequestId(String requestId);

}
