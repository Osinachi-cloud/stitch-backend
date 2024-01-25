package com.stitch.gateway.controller.order;

import com.stitch.commons.exception.StitchException;
import com.stitch.commons.model.dto.PaginatedResponse;
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
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

import static com.stitch.utils.Utils.convertProductOrderToDto;
import static com.stitch.utils.Utils.convertRequestToModel;

//package com.stitch.gateway.controller.order;
//
//
//import com.exquisapps.billanted.commons.model.dto.PaginatedResponse;
//import com.exquisapps.billanted.commons.model.dto.Response;
//import com.exquisapps.billanted.currency.model.enums.Currency;
//import com.exquisapps.billanted.gateway.security.service.AuthenticationService;
//import com.exquisapps.billanted.order.model.dto.OrderReceiptRequest;
//import com.exquisapps.billanted.order.model.dto.OrderReceiptResponse;
//import com.exquisapps.billanted.order.model.dto.OrderRequestDto;
//import com.exquisapps.billanted.order.model.dto.OrderResponseDto;
//import com.exquisapps.billanted.order.model.dto.ScheduleListRequest;
//import com.exquisapps.billanted.order.service.OrderService;
//import com.exquisapps.billanted.payment.model.dto.DailyLimitRequest;
//import com.exquisapps.billanted.payment.model.enums.TierActionType;
//import com.exquisapps.billanted.payment.service.TierConfigService;
//import com.exquisapps.billanted.user.model.dto.CustomerDto;
//import com.exquisapps.billanted.user.service.CustomerService;
//import com.exquisapps.billanted.wallet.service.WalletService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.graphql.data.method.annotation.Argument;
//import org.springframework.graphql.data.method.annotation.MutationMapping;
//import org.springframework.graphql.data.method.annotation.QueryMapping;
//import org.springframework.stereotype.Controller;
//
//import java.time.LocalDateTime;
//import java.time.ZoneOffset;
//import java.util.List;
//
@Controller
@Slf4j
@RequiredArgsConstructor
public class OrderController {

//    private final AuthenticationService authenticationService;
//
//    private final CustomerService customerService;
//
    private final ProductOrderService productOrderService;
//
//    private final WalletService walletService;
//
//    private final TierConfigService tierConfigService;



    @QueryMapping(value = "fetchCustomerOrdersBy")
    public PaginatedResponse<List<ProductOrderDto>> fetchCustomerOrdersBy(
            @Argument Optional<String> productId,
            @Argument Optional<String> customerId,
            @Argument Optional<String> status,
            @Argument Optional<String> orderId,
            @Argument Optional<String> productCategory,
            @Argument Optional<String> vendorId,
            @Argument Optional<Integer> page,
            @Argument Optional<Integer> size) {

        PageRequest pr = PageRequest.of(page.orElse(0),size.orElse(10));
        return productOrderService.fetchCustomerOrdersBy(productId.orElse(null), customerId.orElse(null), status.orElse(null), orderId.orElse(null), productCategory.orElse(null), vendorId.orElse(null) ,pr);
    }

    @MutationMapping(value = "createProductOrder")
    public ProductOrderDto createProductOrder(@Argument("productOrderRequest") ProductOrderRequest productOrderDto ){
//        try {
            return productOrderService.createProductOrder(productOrderDto);
//        }catch (Exception e){
//            throw new StitchException("product could not be created");
//        }
    }


    @QueryMapping(value = "getProductOrderStatsByCustomer")
    public ProductOrderStatistics getProductOrderStatsByCustomer(@Argument("customerId") String customerId ){

        try{
            return productOrderService.getCustomerProductStat(customerId);
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
