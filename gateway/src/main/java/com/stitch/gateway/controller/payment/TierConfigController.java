//package com.stitch.gateway.controller.payment;
//
//import com.exquisapps.billanted.commons.model.dto.Response;
//import com.exquisapps.billanted.payment.model.dto.CardPaymentResponse;
//import com.exquisapps.billanted.payment.model.dto.TierConfigDto;
//import com.exquisapps.billanted.payment.service.TierConfigService;
//import org.springframework.graphql.data.method.annotation.Argument;
//import org.springframework.graphql.data.method.annotation.MutationMapping;
//import org.springframework.graphql.data.method.annotation.QueryMapping;
//import org.springframework.stereotype.Controller;
//
//import java.util.List;
//
//@Controller
//public class TierConfigController {
//
//    private final TierConfigService tierConfigService;
//
//    public TierConfigController(TierConfigService tierConfigService) {
//        this.tierConfigService = tierConfigService;
//    }
//
//
//    @MutationMapping(value = "addTierSetup")
//    public TierConfigDto addTierSetup(@Argument("tierRequest") TierConfigDto tierConfigDto) {
//        return tierConfigService.setUpTier(tierConfigDto);
//    }
//
//    @QueryMapping(value = "fetchAllTierSetup")
//    public List<TierConfigDto> fetchAllTierSetup() {
//        return tierConfigService.fetchAllTierConfig();
//    }
//}
