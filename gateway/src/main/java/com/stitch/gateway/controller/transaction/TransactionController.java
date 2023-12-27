//package com.stitch.gateway.controller.transaction;
//
//import com.exquisapps.billanted.commons.model.dto.PaginatedResponse;
//import com.exquisapps.billanted.commons.model.dto.Response;
//import com.exquisapps.billanted.gateway.security.service.AuthenticationService;
//import com.exquisapps.billanted.payment.model.dto.TransactionDto;
//import com.exquisapps.billanted.payment.model.dto.TransactionHistoryRequest;
//import com.exquisapps.billanted.payment.service.TransactionService;
//import com.exquisapps.billanted.user.model.dto.CustomerDto;
//import lombok.RequiredArgsConstructor;
//import org.springframework.graphql.data.method.annotation.Argument;
//import org.springframework.graphql.data.method.annotation.QueryMapping;
//import org.springframework.stereotype.Controller;
//
//import java.util.List;
//
//@Controller
//@RequiredArgsConstructor
//public class TransactionController {
//
//    private final AuthenticationService authenticationService;
//
//    private final TransactionService transactionService;
//
//
//    @QueryMapping(value = "getTransactionHistory")
//    public PaginatedResponse<List<TransactionDto>> getCustomerTransactions(@Argument("transactionHistoryRequest") TransactionHistoryRequest transactionHistoryRequest) {
//        CustomerDto user = authenticationService.getAuthenticatedUser();
//        return transactionService.fetchCustomerTransactions(user.getCustomerId(), transactionHistoryRequest);
//    }
//
//    @QueryMapping(value = "getAdminTransactionHistory")
//    public PaginatedResponse<List<TransactionDto>> getAdminTransactionHistory(@Argument("transactionHistoryRequest") TransactionHistoryRequest transactionHistoryRequest) {
//        return transactionService.fetchAllTransactions(transactionHistoryRequest);
//    }
//}
