//package com.stitch.payment.service.impl;
//
//import com.exquisapps.billanted.billers.exception.BillingException;
//import com.exquisapps.billanted.billers.service.BillerService;
//import com.exquisapps.billanted.billers.service.impl.BillerServiceImpl;
//import org.junit.Before;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import java.math.BigDecimal;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.lenient;
//
//@ExtendWith(MockitoExtension.class)
//class PaymentBillerServiceImplTest {
//
//        @Mock
//        private BillerService billerService;
//
//        @Mock
//        private PaymentBillerServiceImpl paymentService;
//
//        @Mock
//        private AirtimeVendor airtimeVendor;
//
//        @Mock
//        private DataVendor dataVendor;
//
//        @Mock
//        private CableTvVendor cableTvVendor;
//
//        @Mock
//        private ElectricityVendor electricityVendor;
//
//        @Mock
//        private RequeryService requeryService;
//
//        @Before
//        public void setUp() {
//            MockitoAnnotations.initMocks(this);
//            paymentService = new PaymentBillerServiceImpl(billerService);
//        }
//
//        @Test
//         void testVendAirtime_Success() {
//
//            AirtimeRequest airtimeRequest = new AirtimeRequest();
//            airtimeRequest.setServiceID("AIRTIME");
//            airtimeRequest.setPhone("08034567890");
//            airtimeRequest.setAmount("1000");
//            airtimeRequest.setOrderId("ORDER-123456");
//            airtimeRequest.setCustomerId("CUSTOMER-123456");
//            airtimeRequest.setRequest_id("20231109103544RQe1234567890abcdef");
//
//            VTPassAirtimeRequest airtimePaymentRequest = VTPassAirtimeRequest.builder()
//                .serviceId(airtimeRequest.getServiceID())
//                .phone(airtimeRequest.getPhone())
//                .amount(airtimeRequest.getAmount())
//                .orderId(airtimeRequest.getOrderId())
//                .customerId(airtimeRequest.getCustomerId())
//                .build();
//
//            VTPassAirtimePaymentResponse expectedResponse = VTPassAirtimePaymentResponse.builder()
//                .amount(airtimePaymentRequest.getAmount())
//                .requestId(airtimePaymentRequest.getRequestId())
//                .build();
//
//            lenient().when(paymentService.vendAirtime(airtimeRequest)).thenReturn(expectedResponse);
//
//            VTPassAirtimePaymentResponse actualResponse = VTPassAirtimePaymentResponse.builder()
//                .amount("1000")
//                .requestId("1")
//                .build();
//
//            assertEquals(expectedResponse, actualResponse);
//        }
//
//
//    @Test
//    void testVendAirtime_BillingException() throws BillingException {
//
//        AirtimeRequest airtimeRequest = new AirtimeRequest();
//        airtimeRequest.setServiceID("AIRTIME");
//        airtimeRequest.setPhone("08034567890");
//        airtimeRequest.setAmount("1000");
//        airtimeRequest.setOrderId("ORDER-123456");
//        airtimeRequest.setCustomerId("CUSTOMER-123456");
//
//        Mockito.when(airtimeVendor.vendAirTime(any(VTPassAirtimeRequest.class))).thenThrow(new BillingException());
//
//        billerService = new BillerServiceImpl(airtimeVendor, null, null, null, null, null);
//
//        assertThrows(BillingException.class, () -> billerService.vendAirtime(airtimeRequest));
//    }
//
//    @Test
//    void testVendData_Success() {
//
//        DataRequest dataRequest = new DataRequest();
//
//        dataRequest.setPhone("08034567890");
//        dataRequest.setAmount("1000");
//        dataRequest.setOrderId("ORDER-123456");
//        dataRequest.setCustomerId("CUSTOMER-123456");
//        dataRequest.setRequest_id("20231109103544RQe1234567890abcdef");
//
//        VTPassDataRequest dataPaymentRequest = VTPassDataRequest.builder()
//                .serviceID(dataRequest.getServiceID())
//                .phone(dataRequest.getPhone())
//                .amount(dataRequest.getAmount())
//                .orderId(dataRequest.getOrderId())
//                .customerId(dataRequest.getCustomerId())
//                .build();
//
//        VTPassDataPaymentResponseDto expectedResponse = VTPassDataPaymentResponseDto.builder()
//                .amount(dataPaymentRequest.getAmount())
//                .requestId(dataPaymentRequest.getRequest_id())
//                .build();
//
//        lenient().when(paymentService.vendData(dataRequest)).thenReturn(expectedResponse);
//
//        VTPassDataPaymentResponseDto actualResponse = VTPassDataPaymentResponseDto.builder()
//                .amount("1000")
//                .requestId("20231109103544RQe1234567890abcdef")
//                .build();
//
//        assertEquals(expectedResponse, actualResponse);
//    }
//
//    @Test
//    void testVendData_BillingException() throws BillingException {
//
//        DataRequest dataRequest = new DataRequest();
//
//        dataRequest.setPhone("08034567890");
//        dataRequest.setAmount("1000");
//        dataRequest.setOrderId("ORDER-123456");
//        dataRequest.setCustomerId("CUSTOMER-123456");
//        dataRequest.setRequest_id("20231109103544RQe1234567890abcdef");
//
//        Mockito.when(dataVendor.vendData(any(VTPassDataRequest.class))).thenThrow(new BillingException());
//
//        billerService = new BillerServiceImpl(airtimeVendor, dataVendor, null, null, null, null);
//
//        assertThrows(BillingException.class, () -> billerService.vendData(dataRequest));
//    }
//
//
//    @Test
//    void testVendCableTv_Success() {
//
//        CableTvRequest cableTvRequest = new CableTvRequest();
//
//        cableTvRequest.setPhone("08034567890");
//        cableTvRequest.setAmount("1000");
//        cableTvRequest.setOrderId("ORDER-123456");
//        cableTvRequest.setCustomerId("CUSTOMER-123456");
//        cableTvRequest.setRequest_id("20231109103544RQe1234567890abcdef");
//
//        VTPassCableTvPaymentRequest cableTvPaymentRequest = VTPassCableTvPaymentRequest.builder()
//                .serviceID(cableTvRequest.getServiceID())
//                .phone(cableTvRequest.getPhone())
//                .amount(cableTvRequest.getAmount())
//                .orderId(cableTvRequest.getOrderId())
//                .customerId(cableTvRequest.getCustomerId())
//                .build();
//
//        VTPassCableTvPaymentResponseDto expectedResponse = VTPassCableTvPaymentResponseDto.builder()
//                .amount(cableTvPaymentRequest.getAmount())
//                .requestId(cableTvPaymentRequest.getRequest_id())
//                .build();
//
//        lenient().when(paymentService.vendCableTv(cableTvRequest)).thenReturn(expectedResponse);
//
//        VTPassCableTvPaymentResponseDto actualResponse = VTPassCableTvPaymentResponseDto.builder()
//                .amount("1000")
//                .requestId("20231109103544RQe1234567890abcdef")
//                .build();
//
//        assertEquals(expectedResponse, actualResponse);
//    }
//
//    @Test
//    void testVendCableTv_BillingException() throws BillingException {
//
//        CableTvRequest cableTvRequest = new CableTvRequest();
//
//        cableTvRequest.setPhone("08034567890");
//        cableTvRequest.setAmount("1000");
//        cableTvRequest.setOrderId("ORDER-123456");
//        cableTvRequest.setCustomerId("CUSTOMER-123456");
//        cableTvRequest.setRequest_id("20231109103544RQe1234567890abcdef");
//
//        Mockito.when(cableTvVendor.vendCablePlan(any(VTPassCableTvPaymentRequest.class))).thenThrow(new BillingException());
//
//        billerService = new BillerServiceImpl(airtimeVendor, dataVendor, null, cableTvVendor, null, null);
//
//        assertThrows(BillingException.class, () -> billerService.vendCableTv(cableTvRequest));
//    }
//
//    @Test
//    void testVendElectricity_Success() {
//
//        ElectricityRequest electricityRequest = new ElectricityRequest();
//
//        electricityRequest.setPhone("08034567890");
//        electricityRequest.setAmount(BigDecimal.TEN);
//        electricityRequest.setOrderId("ORDER-123456");
//        electricityRequest.setCustomerId("CUSTOMER-123456");
//        electricityRequest.setRequest_id("20231109103544RQe1234567890abcdef");
//
//        VTPassElectricityPaymentRequest electricityPaymentRequest = VTPassElectricityPaymentRequest.builder()
//                .serviceID(electricityRequest.getServiceID())
//                .phone(electricityRequest.getPhone())
//                .amount(electricityRequest.getAmount())
//                .orderId(electricityRequest.getOrderId())
//                .customerId(electricityRequest.getCustomerId())
//                .build();
//
//        ElectricityPaymentResponse expectedResponse = ElectricityPaymentResponse.builder()
//                .amount(electricityPaymentRequest.getAmount().toString())
//                .requestId(electricityPaymentRequest.getRequest_id())
//                .build();
//
//        lenient().when(paymentService.vendElectricity(electricityRequest)).thenReturn(expectedResponse);
//
//        ElectricityPaymentResponse actualResponse = ElectricityPaymentResponse.builder()
//                .amount("10")
//                .requestId("20231109103544RQe1234567890abcdef")
//                .build();
//
//        assertEquals(expectedResponse, actualResponse);
//    }
//
//    @Test
//    void testVendElectricity_BillingException() throws BillingException {
//
//        ElectricityRequest electricityRequest = new ElectricityRequest();
//
//        electricityRequest.setPhone("08034567890");
//        electricityRequest.setAmount(BigDecimal.TEN);
//        electricityRequest.setOrderId("ORDER-123456");
//        electricityRequest.setCustomerId("CUSTOMER-123456");
//        electricityRequest.setRequest_id("20231109103544RQe1234567890abcdef");
//
//        billerService = new BillerServiceImpl(airtimeVendor, dataVendor, electricityVendor, cableTvVendor, null, null);
//
//        assertThrows(BillingException.class, () -> billerService.vendElectricity(electricityRequest));
//    }
//
//    @Test
//    void testRequeryTransaction_Success() {
//
//        BillPaymentRequest billPaymentRequest = BillPaymentRequest.builder()
//                .requestId("20231109103544RQe1234567890abcdef")
//                .serviceId("SERVICE_123456")
//                .customerId("CUSTOMER-123456")
//                .orderId("ORDER-123456")
//                .category("CATEGORY")
//                .build();
//
//        VTPassRequeryRequest queryRequest = new VTPassRequeryRequest();
//        queryRequest.setRequestId(billPaymentRequest.getRequestId());
//
//        VTPassRequeryResponse expectedResponse = VTPassRequeryResponse.builder()
//                .requestId(queryRequest.getRequestId())
//                .amount("10")
//                .build();
//
//        lenient().when(paymentService.requeryTransaction(billPaymentRequest)).thenReturn(expectedResponse);
//
//        VTPassRequeryResponse actualResponse = VTPassRequeryResponse.builder()
//                .amount("10")
//                .requestId("20231109103544RQe1234567890abcdef")
//                .build();
//
//        assertEquals(expectedResponse, actualResponse);
//    }
//
//    @Test
//    void testRequeryTransaction_BillingException() throws BillingException {
//
//        BillPaymentRequest billPaymentRequest = BillPaymentRequest.builder()
//                .requestId("20231109103544RQe1234567890abcdef")
//                .serviceId("SERVICE_123456")
//                .customerId("CUSTOMER-123456")
//                .orderId("ORDER-123456")
//                .category("CATEGORY")
//                .build();
//
//        Mockito.when(requeryService.query(any(VTPassRequeryRequest.class))).thenThrow(new BillingException());
//
//        billerService = new BillerServiceImpl(airtimeVendor, dataVendor, electricityVendor, cableTvVendor, null, requeryService);
//
//        assertThrows(BillingException.class, () -> billerService.requeryTransaction(billPaymentRequest));
//    }
//
//}