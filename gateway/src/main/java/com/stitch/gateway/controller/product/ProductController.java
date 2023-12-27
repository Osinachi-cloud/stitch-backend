//package com.stitch.gateway.controller.product;
//
//
//import com.exquisapps.billanted.billers.model.dtos.*;
//import com.exquisapps.billanted.product.model.dto.CableInfoDto;
//import com.exquisapps.billanted.product.model.dto.ProductCategoryDto;
//import com.exquisapps.billanted.product.model.dto.ProviderDto;
//import com.exquisapps.billanted.product.service.ProductService;
//import org.springframework.graphql.data.method.annotation.Argument;
//import org.springframework.graphql.data.method.annotation.QueryMapping;
//import org.springframework.stereotype.Controller;
//
//import java.util.List;
//
//@Controller
//public class ProductController {
//
//    private final ProductService productService;
//
//    public ProductController(ProductService productService) {
//        this.productService = productService;
//    }
//
//
//    @QueryMapping(value = "getAllProductCategories")
//    public List<ProductCategoryDto> getProductCategories() {
//        return productService.getAllProductCategories();
//    }
//
//    @QueryMapping(value = "getActiveProductCategories")
//    public List<ProductCategoryDto> getActiveProductCategories() {
//        return productService.getActiveProductCategories();
//    }
//
//    @QueryMapping(value = "getAllProviders")
//    public List<ProviderDto> getProviders(@Argument("categoryName") String categoryName) {
//        return productService.getAllProvidersByCategoryName(categoryName);
//    }
//
//    @QueryMapping(value = "getActiveProviders")
//    public List<ProviderDto> getActiveProviders(@Argument("categoryName") String categoryName) {
//        return productService.getActiveProvidersByCategoryName(categoryName);
//    }
//
//
//    @QueryMapping(value = "getDataPackages")
//    public DataPlanDto getDataPlan(@Argument("productCode") String productCode) {
//        return  productService.getDataPackages(productCode);
//    }
//
//    @QueryMapping(value = "getCableTvPackages")
//    public VTPassCablePackageDto getCableTvPackage(@Argument("cableTVType") String cableTVType) {
//        return  productService.getCableTvPackages(cableTVType);
//    }
//
//
//    @QueryMapping(value = "cableInfoEnquiry")
//    public VTPassCableInfoResponse cableInfoEnquiry(@Argument("cableData") CableInfoDto cableInfoDto) {
//        return  productService.cableInfoEnquiry(cableInfoDto.getServiceID(), cableInfoDto.getSmartCard());
//    }
//
//    @QueryMapping(value = "verifyInternetUserEmail")
//    public VTPassDataEmailVerificationResponse verifyInternetUserEmail(@Argument("emailVerificationRequest") InternetEmailVerificationRequest internetEmailVerificationRequest) {
//        return  productService.verifyInternetUserEmail(internetEmailVerificationRequest.getServiceId(), internetEmailVerificationRequest.getEmailAddress());
//    }
//
//    @QueryMapping(value = "meterNumberValidation")
//    public MeterValidationResponse meterNumberValidation(@Argument("meterData") ElectricityMeterValidationRequest request) {
//        return  productService.validateMeterNumber(request);
//    }
//}
