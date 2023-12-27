//package com.stitch.gateway.controller.user;
//
//import com.exquisapps.billanted.commons.model.dto.CountryDto;
//import com.exquisapps.billanted.commons.service.CountryService;
//import com.exquisapps.billanted.gateway.security.model.Unsecured;
//import org.springframework.graphql.data.method.annotation.QueryMapping;
//import org.springframework.stereotype.Controller;
//
//import java.util.List;
//
//@Controller
//public class CountryController {
//
//    private final CountryService countryService;
//
//    public CountryController(CountryService countryService) {
//        this.countryService = countryService;
//    }
//
//
//    @Unsecured
//    @QueryMapping(value = "getCountries")
//    public List<CountryDto> getCountries(){
//        return countryService.getActiveCountries();
//    }
//
//}
