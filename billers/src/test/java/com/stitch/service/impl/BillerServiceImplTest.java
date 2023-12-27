//package com.stitch.service.impl;
//
//import com.exquisapps.billanted.billers.model.dtos.*;
//import com.stitch.model.entity.BillTransaction;
//import com.stitch.service.BillerService;
//import com.stitch.service.BillerTransactionService;
//import com.exquisapps.billanted.billers.vendor.vtpass.*;
//import com.stitch.model.dtos.*;
//import com.stitch.vendor.vtpass.*;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.math.BigDecimal;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith({SpringExtension.class})
//@ContextConfiguration(classes = BillerServiceImpl.class)
//class BillerServiceImplTest {
//
//    @MockBean
//    AirtimeVendor airtimeVendor;
//    @MockBean
//    DataVendor dataVendor;
//    @MockBean
//    ElectricityVendor electricityVendor;
//    @MockBean
//    CableTvVendor cableTvVendor;
//    @MockBean
//    BillerTransactionService billerTransactionService;
//
//    @MockBean
//    RequeryService requeryService;
//
//    @Autowired
//    BillerService billerService;
//
//    @Test
//    void vendAirtime() {
//
//        AirtimeRequest airtimePaymentRequest = AirtimeRequest.builder()
//                .serviceID("mtn")
//                .phone("080059690596")
//                .amount("100")
//                .orderId("9494803480455884")
//                .customerId("3212345678")
//                .build();
//
//        VTPassAirtimePaymentResponse.Transactions transaction = new VTPassAirtimePaymentResponse.Transactions();
//        transaction.setStatus("delivered");
//        transaction.setTotalAmount(96);
//        transaction.setCommission(4);
//        VTPassAirtimePaymentResponse.Content content = new VTPassAirtimePaymentResponse.Content();
//        content.setTransactions(transaction);
//
//        VTPassAirtimePaymentResponse airtimePaymentResponse = VTPassAirtimePaymentResponse.builder()
//                .code("000")
//                .responseDescription("Transaction Successful")
//                .amount("100")
//                .content(content)
//                .build();
//
//        when(airtimeVendor.vendAirTime(any(VTPassAirtimeRequest.class))).thenReturn(airtimePaymentResponse);
//        when(billerTransactionService.saveBillTransaction(any(BillTransaction.class))).thenReturn(new BillTransaction());
//
//        VTPassAirtimePaymentResponse paymentResponse = billerService.vendAirtime(airtimePaymentRequest);
//
//        assertEquals("000", paymentResponse.getCode());
//    }
//
//    @Test
//    void vendData() {
//
//        DataRequest dataPaymentRequest = DataRequest.builder()
//                .serviceID("mtn")
//                .phone("080059690596")
//                .billersCode("080059690596")
//                .amount("100")
//                .orderId("9494803480455884")
//                .customerId("3212345678")
//                .build();
//
//        VTPassDataPaymentResponseDto.Transactions transaction = new VTPassDataPaymentResponseDto.Transactions();
//        transaction.setStatus("delivered");
//        transaction.setTotalAmount(96);
//        transaction.setCommission(4);
//        VTPassDataPaymentResponseDto.Content content = new VTPassDataPaymentResponseDto.Content();
//        content.setTransactions(transaction);
//
//        VTPassDataPaymentResponseDto dataPaymentResponse = VTPassDataPaymentResponseDto.builder()
//                .code("000")
//                .responseDescription("Transaction Successful")
//                .amount("100")
//                .content(content)
//                .build();
//
//        when(dataVendor.vendData(any(VTPassDataRequest.class))).thenReturn(dataPaymentResponse);
//        when(billerTransactionService.saveBillTransaction(any(BillTransaction.class))).thenReturn(new BillTransaction());
//
//        VTPassDataPaymentResponseDto paymentResponse = billerService.vendData(dataPaymentRequest);
//
//        assertEquals("000", paymentResponse.getCode());
//    }
//
//    @Test
//    void vendCableTv() {
//
//        CableTvRequest cableTvPaymentRequest = CableTvRequest.builder()
//                .customerId("2345654321")
//                .orderId("93489928383938")
//                .serviceID("dstv")
//                .billersCode("1234675445")
//                .variation_code("premium-package")
//                .amount("2500")
//                .phone("0807847833773")
//                .build();
//
//
//        VTPassCableTvPaymentResponseDto.Transactions transaction = new VTPassCableTvPaymentResponseDto.Transactions();
//        transaction.setStatus("delivered");
//        transaction.setTotalAmount(2240);
//        transaction.setCommission(260);
//        VTPassCableTvPaymentResponseDto.Content content = new VTPassCableTvPaymentResponseDto.Content();
//        content.setTransactions(transaction);
//
//        VTPassCableTvPaymentResponseDto cableTvPaymentResponse = VTPassCableTvPaymentResponseDto.builder()
//                .code("000")
//                .responseDescription("Transaction Successful")
//                .amount("100")
//                .content(content)
//                .build();
//
//        when(cableTvVendor.vendCablePlan(any(VTPassCableTvPaymentRequest.class))).thenReturn(cableTvPaymentResponse);
//        when(billerTransactionService.saveBillTransaction(any(BillTransaction.class))).thenReturn(new BillTransaction());
//
//        VTPassCableTvPaymentResponseDto paymentResponse = billerService.vendCableTv(cableTvPaymentRequest);
//
//        assertEquals("000", paymentResponse.getCode());
//
//    }
//
//    @Test
//    void vendElectricity() {
//
//        ElectricityRequest electricityPaymentRequest = ElectricityRequest.builder()
//                .amount(new BigDecimal("5000"))
//                .billersCode("123242424")
//                .customerId("5643567898")
//                .phone("08083954935")
//                .serviceID("ikeja-electric")
//                .orderId("95049839384804")
//                .variation_code("prepaid")
//                .build();
//
//        VTPassElectricityPrePaidResponseDto.Transactions transaction = new VTPassElectricityPrePaidResponseDto.Transactions();
//        transaction.setStatus("delivered");
//        transaction.setTotalAmount(4960);
//        transaction.setCommission(40);
//        transaction.setConvenienceFee(0);
//        VTPassElectricityPrePaidResponseDto.Content content = new VTPassElectricityPrePaidResponseDto.Content();
//        content.setTransactions(transaction);
//
//        VTPassElectricityPrePaidResponseDto prePaidResponseDto = VTPassElectricityPrePaidResponseDto.builder()
//                .code("000")
//                .responseDescription("Transaction Successful")
//                .amount("10000")
//                .purchasedCode("Token: 0950239305943053930")
//                .content(content)
//                .build();
//
//        when(electricityVendor.vendPrepaidElectricity(any(VTPassElectricityPaymentRequest.class))).thenReturn(prePaidResponseDto);
//        when(billerTransactionService.saveBillTransaction(any(BillTransaction.class))).thenReturn(new BillTransaction());
//
//        ElectricityPaymentResponse paymentResponse = billerService.vendElectricity(electricityPaymentRequest);
//
//        assertEquals("000", paymentResponse.getCode());
//    }
//
//
//    @Test
//    void requeryTransaction() {
//
//        BillPaymentRequest billPaymentRequest = BillPaymentRequest.builder()
//                .requestId("20231101h9e938939")
//                .orderId("939202395059750925")
//                .build();
//
//        VTPassRequeryResponse.Transactions transaction = new VTPassRequeryResponse.Transactions();
//        transaction.setStatus("delivered");
//        transaction.setTotalAmount(96);
//        transaction.setCommission(4);
//        VTPassRequeryResponse.Content content = new VTPassRequeryResponse.Content();
//        content.setTransactions(transaction);
//
//        VTPassRequeryResponse requeryResponse = VTPassRequeryResponse.builder()
//                .code("000")
//                .responseDescription("Transaction Successful")
//                .amount("100")
//                .content(content)
//                .build();
//
//        when(requeryService.query(any(VTPassRequeryRequest.class))).thenReturn(requeryResponse);
//        when(billerTransactionService.saveBillTransaction(any(BillTransaction.class))).thenReturn(new BillTransaction());
//
//        VTPassRequeryResponse response = billerService.requeryTransaction(billPaymentRequest);
//
//        assertEquals("000", response.getCode());
//
//    }
//}