//package com.stitch.payment.service.impl;
//
//import com.stitch.exception.BillingException;
//import com.stitch.model.dtos.*;
//import com.stitch.payment.exception.BillPaymentException;
//import com.stitch.payment.service.PaymentBillerService;
//import com.stitch.service.BillerService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//@Slf4j
//@Service
//public class PaymentBillerServiceImpl implements PaymentBillerService {
//
//    private final BillerService billerService;
//
//    public PaymentBillerServiceImpl(BillerService billerService) {
//        this.billerService = billerService;
//    }
//
//
//    @Override
//    public VTPassAirtimePaymentResponse vendAirtime(AirtimeRequest airtimeRequest) {
//
//
//        try {
//            return this.billerService.vendAirtime(airtimeRequest);
//        } catch (BillingException e) {
//            log.error(e.getMessage(), e);
//            throw new BillPaymentException(e.getMessage(), e);
//        }
//    }
//
//    @Override
//    public VTPassDataPaymentResponseDto vendData(DataRequest dataRequest) {
//
//        try {
//            return this.billerService.vendData(dataRequest);
//        } catch (BillingException e) {
//            log.error(e.getMessage(), e);
//            throw new BillPaymentException(e.getMessage(), e);
//        }
//    }
//
//    @Override
//    public VTPassCableTvPaymentResponseDto vendCableTv(CableTvRequest cableRequest) {
//
//        try {
//            return this.billerService.vendCableTv(cableRequest);
//        } catch (BillingException e) {
//            log.error(e.getMessage(), e);
//            throw new BillPaymentException(e.getMessage(), e);
//        }
//    }
//
//    @Override
//    public ElectricityPaymentResponse vendElectricity(ElectricityRequest electricityRequest) {
//
//
//        try {
//           return this.billerService.vendElectricity(electricityRequest);
//        } catch (BillingException e) {
//            log.error(e.getMessage(), e);
//            throw new BillPaymentException(e.getMessage(), e);
//        }
//    }
//
//    @Override
//    public VTPassRequeryResponse requeryTransaction(BillPaymentRequest billPaymentRequest){
//
//        try {
//            return this.billerService.requeryTransaction(billPaymentRequest);
//        } catch (BillingException e) {
//            log.error(e.getMessage(), e);
//            throw new BillPaymentException(e.getMessage(), e);
//        }
//    }
//
//    @Override
//    public VTPassRequeryResponse requeryTransaction(String orderId) {
//
//        try {
//            return this.billerService.requeryTransaction(orderId);
//        } catch (BillingException e) {
//            log.error(e.getMessage(), e);
//            throw new BillPaymentException(e.getMessage(), e);
//        }    }
//}
