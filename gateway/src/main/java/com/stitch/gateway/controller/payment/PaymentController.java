//package com.stitch.gateway.controller.payment;
//
//import com.exquisapps.billanted.commons.model.dto.Response;
//import com.exquisapps.billanted.gateway.security.service.AuthenticationService;
//import com.exquisapps.billanted.payment.model.dto.CardPaymentResponse;
//import com.exquisapps.billanted.payment.service.PaymentService;
//import com.exquisapps.billanted.psp.model.dto.PaymentIntentRequest;
//import com.exquisapps.billanted.psp.model.dto.PaymentIntentResponse;
//import com.exquisapps.billanted.user.model.dto.CustomerDto;
//import org.springframework.graphql.data.method.annotation.Argument;
//import org.springframework.graphql.data.method.annotation.MutationMapping;
//import org.springframework.graphql.data.method.annotation.Argument;
//import org.springframework.graphql.data.method.annotation.MutationMapping;
//import org.springframework.graphql.data.method.annotation.QueryMapping;
//import org.springframework.stereotype.Controller;
//
//import java.util.List;
//
//@Controller
//public class PaymentController {
//
//    private final PaymentService paymentService;
//
//    private final AuthenticationService authenticationService;
//
//    public PaymentController(PaymentService paymentService, AuthenticationService authenticationService) {
//        this.paymentService = paymentService;
//        this.authenticationService = authenticationService;
//    }
//
//    @QueryMapping(value = "getPaymentCards")
//    public List<CardPaymentResponse>  getPaymentCards() {
//        String customerId = authenticationService.getAuthenticatedCustomerId();
//        return paymentService.getCustomerCards(customerId);
//    }
//
//
//    @MutationMapping(value = "deleteCard")
//    public Response deleteCard(@Argument("cardId") String cardId) {
//        String customerId = authenticationService.getAuthenticatedCustomerId();
//        return paymentService.deleteCard(customerId, cardId);
//    }
//}
