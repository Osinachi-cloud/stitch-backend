//package com.stitch.gateway.controller.order;
//
//import com.exquisapps.billanted.gateway.security.service.AuthenticationService;
//import com.exquisapps.billanted.order.model.dto.BeneficiaryResponse;
//import com.exquisapps.billanted.order.model.dto.OrderRequestDto;
//import com.exquisapps.billanted.order.model.dto.OrderResponseDto;
//import com.exquisapps.billanted.order.model.enums.ServiceType;
//import com.exquisapps.billanted.order.service.BeneficiaryService;
//import com.exquisapps.billanted.user.model.dto.CustomerDto;
//import lombok.RequiredArgsConstructor;
//import org.springframework.graphql.data.method.annotation.Argument;
//import org.springframework.graphql.data.method.annotation.MutationMapping;
//import org.springframework.graphql.data.method.annotation.QueryMapping;
//import org.springframework.stereotype.Controller;
//
//import java.util.List;
//
//@Controller
//@RequiredArgsConstructor
//public class BeneficiaryController {
//
//    private final AuthenticationService authenticationService;
//
//    private final BeneficiaryService beneficiaryService;
//
//
//    @QueryMapping(value = "fetchBeneficiary")
//    public List<BeneficiaryResponse> fetchBeneficiary(@Argument("type") ServiceType type) {
//        CustomerDto user = authenticationService.getAuthenticatedUser();
//        String customerId = user.getCustomerId();
//        return beneficiaryService.fetchBeneficiary(customerId, type);
//    }
//}
