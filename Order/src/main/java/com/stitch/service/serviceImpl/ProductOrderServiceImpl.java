package com.stitch.service.serviceImpl;

import com.stitch.commons.exception.StitchException;
import com.stitch.commons.model.dto.PaginatedResponse;
import com.stitch.commons.util.NumberUtils;
import com.stitch.exception.OrderException;
import com.stitch.model.dto.ProductOrderDto;
import com.stitch.model.dto.ProductOrderRequest;
import com.stitch.model.dto.ProductOrderStatistics;
import com.stitch.model.entity.ProductOrder;
import com.stitch.model.enums.OrderStatus;
import com.stitch.repository.ProductOrderRepository;
import com.stitch.service.ProductOrderService;
import com.stitch.user.model.dto.BodyMeasurementDto;
import com.stitch.user.model.entity.BodyMeasurement;
import com.stitch.user.model.entity.UserEntity;
import com.stitch.user.repository.BodyMeasurementRepository;
import com.stitch.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.stitch.commons.util.Mapper.convertModelToDto;
import static com.stitch.commons.util.SharedUtils.getLoggedInUser;
import static com.stitch.utils.Utils.*;
import static java.lang.Math.toIntExact;

@Service
public class ProductOrderServiceImpl implements ProductOrderService {

    private final Logger log = LoggerFactory.getLogger(ProductOrderServiceImpl.class);

    private final ProductOrderRepository productOrderRepository;

    private final BodyMeasurementRepository bodyMeasurementRepository;

    private final UserRepository userRepository;

    public ProductOrderServiceImpl(ProductOrderRepository productOrderRepository, BodyMeasurementRepository bodyMeasurementRepository, UserRepository userRepository) {
        this.productOrderRepository = productOrderRepository;
        this.bodyMeasurementRepository = bodyMeasurementRepository;
        this.userRepository = userRepository;
    }

    @Override
    public PaginatedResponse<List<ProductOrderDto>> fetchCustomerOrders(String productId, String emailAddress, String status, String orderId, String productCategory, PageRequest pr) {

        Page<ProductOrder> orderPage = productOrderRepository.fetchCustomerOrdersBy(productId, emailAddress, status, orderId, productCategory, pr);
        return mapResponse(orderPage);

    }

    private PaginatedResponse<List<ProductOrderDto>> mapResponse(Page<ProductOrder> orderPage) {
        List<ProductOrder> productOrderList = orderPage.getContent();
        log.info("productOrderList ====>>>  : {}", productOrderList);
        List<ProductOrderDto> productOrderDtoList = orderListToDto(productOrderList);

        PaginatedResponse<List<ProductOrderDto>> paginatedResponse = new PaginatedResponse<>();
        paginatedResponse.setPage(orderPage.getNumber());
        paginatedResponse.setData(productOrderDtoList);
        paginatedResponse.setSize(orderPage.getSize());
        paginatedResponse.setTotal(toIntExact(orderPage.getTotalElements()));

        return paginatedResponse;

    }

    @Override
    public PaginatedResponse<List<ProductOrderDto>> fetchCustomerOrders(String productId, String emailAddress, String status, String orderId, String productCategory, int page, int size) {
        try {
            PageRequest pageRequest = PageRequest.of(page, size);
            Page<ProductOrder> orderPage = productOrderRepository.fetchCustomerOrdersBy(productId, emailAddress, status, orderId, productCategory, pageRequest);
            return mapResponse(orderPage);
        } catch (Exception e) {
            log.error("An error occurred fetching customer Orders with order ID :: {} => {}", orderId, e.getMessage());
            throw new OrderException("Failed to fetch Orders for customer", 417);
        }

    }


    @Override
    public PaginatedResponse<List<ProductOrderDto>> fetchVendorOrders(String productId, String emailAddress, String status, String orderId, String productCategory, int page, int size) {
        try {
            PageRequest pageRequest = PageRequest.of(page, size);
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Page<ProductOrder> orderPage = productOrderRepository.fetchVendorOrdersBy(productId, username, status, orderId, productCategory, pageRequest);
            return mapResponse(orderPage);
        } catch (Exception e) {
            log.error("An error occurred fetching Vendor Orders with order ID :: {} => {}", orderId, e.getMessage());
            throw new OrderException("Failed to fetch Orders for vendor", 417);
        }
    }

    @Override
    public ProductOrderDto getProductOrder(String productOrderId) {
        Optional<ProductOrder> existingProductOrder = productOrderRepository.findByProductId(productOrderId);
        if (existingProductOrder.isEmpty()) {
            throw new StitchException("product order does not exist " + productOrderId);
        }
        ProductOrder productOrder = existingProductOrder.get();
        ProductOrderDto productOrderDto = new ProductOrderDto();
        return (ProductOrderDto) convertModelToDto(productOrder, productOrderDto);
    }

    @Override
    public ProductOrderDto getOrderByOrderId(String orderId) {
        try {
            ProductOrder productOrder = productOrderRepository.findByOrderId(orderId)
                    .orElseThrow(() -> new OrderException("product order does not exist " + orderId, 404));
            BodyMeasurement bodyMeasurement = findBodyMeasurement(productOrder.getBodyMeasurementTag(), productOrder.getEmailAddress());
            BodyMeasurementDto bodyMeasurementDto = convertBodyMeasurementToModel(bodyMeasurement);
            ProductOrderDto productOrderDto = convertProductOrderToDto(productOrder, bodyMeasurementDto);
            log.info("productOrderDto :{}", productOrderDto);
            return productOrderDto;
        }catch (OrderException e){
            log.error("A custom error occurred while getting order by id : {} :: {}", orderId, e.getMessage());
            throw new OrderException(e.getMessage(), e.getCode());

        }catch (Exception e){
            log.error("An error occurred while getting product order by order Id : {} : {}", orderId, e.getMessage());
            throw new OrderException("Failed to get product Order with id : " + orderId, 417);
        }
    }

    public BodyMeasurement findBodyMeasurement(String id, String username) {
        Optional<UserEntity> existingUser = userRepository.findByEmailAddress(username);
        if (existingUser.isEmpty()) {
            throw new StitchException("user does not exist");
        }
        UserEntity userEntity = existingUser.get();
        Optional<BodyMeasurement> existingBodyMeasurement = bodyMeasurementRepository.findBodyMeasurementByTagAndUserEntity(id, userEntity);
        if (existingBodyMeasurement.isEmpty()) {
            throw new StitchException("Body measurement does not exist");
        }
        return existingBodyMeasurement.get();

    }

    @Override
    public List<ProductOrder> getOrdersByTransactionId(String orderId) {
        return productOrderRepository.findProductOrdersByTransactionId(orderId);
    }

    @Override
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ProductOrderDto createProductOrder(ProductOrderRequest productOrderDto) {
        log.info("create order request  : {}", productOrderDto);
        try {
            productOrderDto.setStatus("PROCESSING");
            productOrderDto.setOrderId(NumberUtils.generate(10));
            log.info("productOrderDto 2: {}", productOrderDto);
            ProductOrder productOrder = convertRequestToModel(productOrderDto);
//            log.info("productOrder: {}", productOrder);
            ProductOrder savedproductOrder = productOrderRepository.save(productOrder);
            return convertProductOrderToDto(savedproductOrder);
        } catch (Exception e) {
            log.error("An error occurred creating product order : {}", e.getMessage());
            throw new OrderException("Failed to create product order", 417);
        }

    }

    @Override
    public ProductOrderDto updateProductOrder(String orderId, String orderStatus) {
        log.info("Request to update Product order with Id : : {} to status :: {}", orderId, orderStatus);
        ProductOrder order = productOrderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new OrderException(String.format("No order found for id : %s", orderId), 404));
        log.info("order returned : {}", order);

        try {
            order.setStatus(OrderStatus.valueOf(orderStatus));
        } catch (Exception e) {
            throw new OrderException("Invalid Order status received", 400);
        }
        ProductOrder savedproductOrder = productOrderRepository.save(order);
        return convertProductOrderToDto(savedproductOrder);
    }

    @Override
    public ProductOrderStatistics getCustomerProductStat() {
        try {

            String userMail = getLoggedInUser().orElseThrow(() -> new OrderException("Failed to authenticate user", 403));
            List<ProductOrder> existingProductOrder = productOrderRepository.findByEmailAddress(userMail);
            if (existingProductOrder.isEmpty()) {
                throw new OrderException("customer with : " + userMail + " does not exist", 404);
            }
            ProductOrderStatistics productOrderStatistics = new ProductOrderStatistics();
            productOrderStatistics.setAllOrdersCount(productOrderRepository.countAllOrdersByCustomerId(userMail));
            productOrderStatistics.setCompletedOrdersCount(productOrderRepository.countCompletedOrdersByCustomerId(userMail));
            productOrderStatistics.setCancelledOrdersCount(productOrderRepository.countCancelledOrdersByCustomerId(userMail));
            productOrderStatistics.setProcessingOrdersCount(productOrderRepository.countProcessingOrdersByCustomerId(userMail));
            productOrderStatistics.setFailedOrdersCount(productOrderRepository.countFailedOrdersByCustomerId(userMail));
            productOrderStatistics.setInTransitOrdersCount(productOrderRepository.countInTransitOrdersByCustomerId(userMail));
            productOrderStatistics.setPaymentCompletedCount(productOrderRepository.countPaymentCompletedOrdersByCustomerId(userMail));
            return productOrderStatistics;
        } catch (OrderException e) {
            log.error("Custom Error occurred in customer Order stats : {}", e.getMessage());
            throw new OrderException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            log.error("An error occurred getting customer's order statistics : {}", e.getMessage());
            throw new OrderException("Failed to get customer's order statistics", 417);
        }

    }

    @Override
    public ProductOrderStatistics getVendorProductStat() {
        try {
            String username = getLoggedInUser().orElseThrow(() -> new OrderException("Failed to authenticate user", 403));
            List<ProductOrder> existingProductOrder = productOrderRepository.findProductOrderByVendorEmailAddress(username);
            if (existingProductOrder.isEmpty()) {
                throw new OrderException("No order found for logged in user", 404);
            }
            ProductOrderStatistics productOrderStatistics = new ProductOrderStatistics();
            productOrderStatistics.setAllOrdersCount(productOrderRepository.countAllOrdersByVendorId(username));
            productOrderStatistics.setCompletedOrdersCount(productOrderRepository.countCompletedOrdersByVendorId(username));
            productOrderStatistics.setCancelledOrdersCount(productOrderRepository.countCancelledOrdersByVendorId(username));
            productOrderStatistics.setProcessingOrdersCount(productOrderRepository.countProcessingOrdersByVendorId(username));
            productOrderStatistics.setFailedOrdersCount(productOrderRepository.countFailedOrdersByVendorId(username));
            productOrderStatistics.setInTransitOrdersCount(productOrderRepository.countInTransitOrdersByVendorId(username));
            productOrderStatistics.setPaymentCompletedCount(productOrderRepository.countPaymentCompletedOrdersByVendorId(username));
            return productOrderStatistics;
        } catch (OrderException e) {
            log.error("Custom Error occurred in Vendor Order stats : {}", e.getMessage());
            throw new OrderException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            log.error("An error occurred getting Vendor's order statistics : {}", e.getMessage());
            throw new OrderException("Failed to get Vendor's order statistics", 417);
        }
    }
}
