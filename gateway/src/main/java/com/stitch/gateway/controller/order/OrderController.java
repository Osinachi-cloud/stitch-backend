package com.stitch.gateway.controller.order;

import com.stitch.commons.exception.StitchException;
import com.stitch.commons.model.dto.PaginatedResponse;
import com.stitch.commons.util.NumberUtils;
import com.stitch.gateway.security.model.CustomUserDetails;
import com.stitch.model.dto.ProductOrderDto;
import com.stitch.model.dto.ProductOrderRequest;
import com.stitch.model.dto.ProductOrderStatistics;
import com.stitch.model.entity.ProductOrder;
import com.stitch.service.ProductOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

import static com.stitch.utils.Utils.convertProductOrderToDto;
import static com.stitch.utils.Utils.convertRequestToModel;


@Controller
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    private final ProductOrderService productOrderService;

    @QueryMapping(value = "fetchCustomerOrdersBy")
    public PaginatedResponse<List<ProductOrderDto>> fetchCustomerOrdersBy(
            @Argument Optional<String> productId,
            @Argument Optional<String> emailAddress,
            @Argument Optional<String> status,
            @Argument Optional<String> orderId,
            @Argument Optional<String> productCategory,
            @Argument Optional<Integer> page,
            @Argument Optional<Integer> size) {

        PageRequest pr = PageRequest.of(page.orElse(0),size.orElse(10));
        return productOrderService.fetchCustomerOrdersBy(productId.orElse(null), emailAddress.orElse(null), status.orElse(null), orderId.orElse(null), productCategory.orElse(null) ,pr);
    }

    @QueryMapping(value = "fetchVendorOrdersBy")
//    @PreAuthorize("hasAuthority('VENDOR')")
    public PaginatedResponse<List<ProductOrderDto>> fetchVendorOrdersBy(
            @Argument Optional<String> productId,
            @Argument Optional<String> status,
            @Argument Optional<String> orderId,
            @Argument Optional<String> productCategory,
            @Argument Optional<Integer> page,
            @Argument Optional<Integer> size) {

        PageRequest pr = PageRequest.of(page.orElse(0),size.orElse(10));
        return productOrderService.fetchVendorOrdersBy(productId.orElse(null), status.orElse(null), orderId.orElse(null), productCategory.orElse(null) ,pr);
    }

    @MutationMapping(value = "createProductOrder")
    public ProductOrderDto createProductOrder(@Argument("productOrderRequest") ProductOrderRequest productOrderDto ){
        try {

        log.info("productOrderDto : {}", productOrderDto);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customer = (CustomUserDetails) authentication.getPrincipal();
        String userId = customer.getUser().getUserId();
//        productOrderDto.setCustomerId(userId);
        productOrderDto.setStatus("PROCESSING");
        productOrderDto.setOrderId(NumberUtils.generate(10));
            log.info("productOrderDto 2: {}", productOrderDto);

            return productOrderService.createProductOrder(productOrderDto);
        }catch (Exception e){
            throw new StitchException("product could not be created");
        }
    }

    @MutationMapping(value = "updateProductOrder")
    public ProductOrderDto updateProductOrder(@Argument("productOrderRequest") ProductOrderRequest productOrderDto ){
        log.info("productOrderDto 00: {}", productOrderDto);
        try {
            return productOrderService.updateProductOrder(productOrderDto);
        }catch (Exception e){
            throw new StitchException("product could not be updated");
        }
    }

    @QueryMapping(value = "getProductOrderStatsByCustomer")
    public ProductOrderStatistics getProductOrderStatsByCustomer(){

        try{
            return productOrderService.getCustomerProductStat();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @QueryMapping(value = "getProductOrderStatsByVendor")
//    @PreAuthorize("hasAuthority('VENDOR')")
    public ProductOrderStatistics getProductOrderStatsByVendor(){

        try{
            return productOrderService.getVendorProductStat();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @QueryMapping(value = "getOrderByOrderId")
//    @PreAuthorize("hasAuthority('VENDOR')")
    public ProductOrderDto getOrderByOrderId(@Argument ("orderId") String orderId){

        try{
            return productOrderService.getOrderByOrderId(orderId);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }




}
//
//
//    @MutationMapping(value = "placeOrder")
//    public OrderResponseDto placeOrder(@Argument("orderRequest") OrderRequestDto orderRequestDto) {
//
//        CustomerDto user = authenticationService.getAuthenticatedUser();
//        String customerId = user.getCustomerId();
//
//        customerService.checkPin(customerId, orderRequestDto.getPin());
//        orderRequestDto.setCustomerId(customerId);
//        orderRequestDto.setEmail(user.getEmailAddress());
//
//        Currency currency = walletService.getCustomerCurrency(customerId);
//
//        tierConfigService.cumulativeDailyLimits(DailyLimitRequest.builder()
//                .currency(orderRequestDto.getCurrency())
//                .actionType(TierActionType.W)
//                .category(orderRequestDto.getCategory())
//                .customerId(customerId)
//                .loadAmount(orderRequestDto.getAmount())
//                .tier(user.getTier())
//            .build());
//
//        return orderService.placeOrder(orderRequestDto);
//    }
//
//    @MutationMapping(value = "initiateOrder")
//    public OrderResponseDto initiateOrder(@Argument("orderRequest") OrderRequestDto orderRequestDto) {
//
//        CustomerDto user = authenticationService.getAuthenticatedUser();
//        String customerId = user.getCustomerId();
//
//        customerService.checkPin(customerId, orderRequestDto.getPin());
//        orderRequestDto.setCustomerId(customerId);
//        orderRequestDto.setEmail(user.getEmailAddress());
//        orderRequestDto.setCustomerName(user.getFirstName()+" "+user.getLastName());
//
//        Currency currency = walletService.getCustomerCurrency(customerId);
//
//        tierConfigService.cumulativeDailyLimits(DailyLimitRequest.builder()
//            .currency(orderRequestDto.getCurrency())
//            .actionType(TierActionType.W)
//            .customerId(customerId)
//            .category(orderRequestDto.getCategory())
//            .loadAmount(orderRequestDto.getAmount())
//            .tier(user.getTier())
//            .build());
//
//        return orderService.initiateOrder(orderRequestDto);
//    }
//
//
//    @MutationMapping(value = "completeOrder")
//    public OrderResponseDto completeOrder(@Argument("orderRequest") OrderRequestDto orderRequestDto) {
//
//        CustomerDto user = authenticationService.getAuthenticatedUser();
//        String customerId = user.getCustomerId();
//
//        customerService.checkPin(customerId, orderRequestDto.getPin());
//        orderRequestDto.setCustomerId(customerId);
//        orderRequestDto.setEmail(user.getEmailAddress());
//
//        return orderService.completeOrder(orderRequestDto);
//    }
//
//    @MutationMapping(value = "scheduleOrder")
//    public Response scheduleOrder(@Argument("orderRequest") OrderRequestDto orderRequestDto) {
//
//        CustomerDto user = authenticationService.getAuthenticatedUser();
//        String customerId = user.getCustomerId();
//
//        customerService.checkPin(customerId, orderRequestDto.getPin());
//        orderRequestDto.setCustomerId(customerId);
//        orderRequestDto.setEmail(user.getEmailAddress());
//        orderRequestDto.setCustomerName(user.getFirstName()+" "+user.getLastName());
//
//        return orderService.scheduleOrder(orderRequestDto);
//    }
//
//    @QueryMapping(value = "getScheduleOrders")
//    public PaginatedResponse<List<OrderRequestDto>> getScheduleOrders(@Argument("scheduleListRequest") ScheduleListRequest request) {
//
//        CustomerDto user = authenticationService.getAuthenticatedUser();
//        String customerId = user.getCustomerId();
//
//        return orderService.getScheduleOrders(customerId, request);
//    }
//
//    @MutationMapping(value = "updateScheduleOrder")
//    public Response updateScheduleOrder(@Argument("orderRequest") OrderRequestDto orderRequestDto) {
//
//        CustomerDto user = authenticationService.getAuthenticatedUser();
//        String customerId = user.getCustomerId();
//
//        customerService.checkPin(customerId, orderRequestDto.getPin());
//        orderRequestDto.setCustomerId(customerId);
//        orderRequestDto.setEmail(user.getEmailAddress());
//        orderRequestDto.setCustomerName(user.getFirstName()+" "+user.getLastName());
//
//        return orderService.updateScheduleOrder(orderRequestDto);
//    }
//
//    @MutationMapping(value = "cancelScheduleOrder")
//    public Response cancelScheduleOrder(@Argument("orderId") String orderId) {
//
//        CustomerDto user = authenticationService.getAuthenticatedUser();
//        String customerId = user.getCustomerId();
//
//        return orderService.cancelScheduleOrder(orderId, customerId);
//    }
//
//
//    @QueryMapping(value = "generateReceipt")
//    public OrderReceiptResponse generateReceipt(@Argument("transactionId") String transactionId) {
//
//        CustomerDto user = authenticationService.getAuthenticatedUser();
//        String customerId = user.getCustomerId();
//        String name = String.format("%s %s", user.getFirstName(), user.getLastName());
//
//        return orderService.generateOrderReceipt(OrderReceiptRequest.builder().customerId(customerId)
//            .name(name).transactionId(transactionId).build());
//    }
//
//    @QueryMapping(value = "adminRequeryOrder")
//    public OrderResponseDto adminRequeryOrder(@Argument("orderId") String orderId) {
//        return orderService.adminRequeryOrder(orderId);
//    }
//}
