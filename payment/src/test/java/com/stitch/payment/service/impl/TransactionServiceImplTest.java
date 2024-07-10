//package com.stitch.payment.service.impl;
//
//import com.stitch.payment.model.dto.TransactionHistoryRequest;
//import com.stitch.payment.model.dto.TransactionResponse;
//import com.stitch.payment.model.entity.Transaction;
//import com.stitch.payment.model.enums.TransactionStatus;
//import com.stitch.payment.repository.TransactionRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.time.ZoneOffset;
//import java.util.Collections;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.Assert.assertThat;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class TransactionServiceImplTest {
//
//    @Mock
//    private TransactionRepository mockTransactionRepository;
//
//    private TransactionServiceImpl transactionServiceImplUnderTest;
//
//    @BeforeEach
//    void setUp() {
//        transactionServiceImplUnderTest = new TransactionServiceImpl(mockTransactionRepository);
//    }
//
////    @Test
//    void testFetchAllCustomerTransactions() {
//        final TransactionHistoryRequest request = new TransactionHistoryRequest();
//        request.setPage(1);
//        request.setSize(1);
//
//        final PaginatedResponse<List<TransactionResponse>> expectedResult = new PaginatedResponse<>();
//        expectedResult.setPage(1);
//        expectedResult.setSize(1);
//        expectedResult.setTotal(1);
//        final TransactionResponse response = new TransactionResponse();
//        response.setId(0L);
//        response.setTransactionId("transactionId");
//        response.setOrderId("orderId");
//        response.setCustomerId("1234567");
//        response.setAmount(new BigDecimal("0.00"));
//        response.setSrcCurrency("NGN");
//        response.setDestCurrency("NGN");
//        response.setFee(new BigDecimal("0.00"));
//        response.setNarration("narration");
//        response.setDescription("description");
//        response.setStatus("C");
//        response.setDateCreated(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC).toEpochMilli());
//        response.setLastUpdated(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC).toEpochMilli());
//        expectedResult.setData(List.of(response));
//
//        final Transaction transaction = new Transaction();
//        transaction.setId(0L);
//        transaction.setDateCreated(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC));
//        transaction.setLastUpdated(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC));
//        transaction.setTransactionId("transactionId");
//        transaction.setOrderId("orderId");
//        transaction.setCustomerId("1234567");
//        transaction.setAmount(new BigDecimal("0.00"));
//        transaction.setSrcCurrency(Currency.NGN);
//        transaction.setDestCurrency(Currency.NGN);
//        transaction.setFee(new BigDecimal("0.00"));
//        transaction.setNarration("narration");
//        transaction.setDescription("description");
//        transaction.setStatus(TransactionStatus.C);
//        final Page<Transaction> transactions = new PageImpl<>(List.of(transaction));
//        when(mockTransactionRepository.findAllByCustomerId("1234567", PageRequest.of(1, 1)))
//                .thenReturn(transactions);
//
//        final PaginatedResponse<List<TransactionResponse>> result = transactionServiceImplUnderTest.fetchAllCustomerTransactions(
//                "1234567", request);
//
//        assertThat(result).isEqualTo(expectedResult);
//    }
//
//    @Test
//    void testFetchAllCustomerTransactions_TransactionRepositoryReturnsNoItems() {
//        final TransactionHistoryRequest request = new TransactionHistoryRequest();
//        request.setPage(0);
//        request.setSize(1);
//
//        final PaginatedResponse<List<TransactionResponse>> expectedResult = new PaginatedResponse<>();
//        expectedResult.setPage(0);
//        expectedResult.setSize(1);
//        expectedResult.setTotal(1);
//        expectedResult.setData(Collections.emptyList());
//
//        when(mockTransactionRepository.findAllByCustomerId("1234567", PageRequest.of(0, 1)))
//                .thenReturn(new PageImpl<>(Collections.emptyList()));
//        final PaginatedResponse<List<TransactionResponse>> result = transactionServiceImplUnderTest.fetchAllCustomerTransactions(
//                "1234567", request);
//        assertThat(result).isEqualTo(expectedResult);
//    }
//}
