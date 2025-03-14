package com.stitch.service.serviceImpl;

import com.stitch.commons.exception.StitchException;
import com.stitch.commons.model.dto.PaginatedResponse;
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
    public PaginatedResponse<List<ProductOrderDto>> fetchCustomerOrdersBy(String productId, String emailAddress , String status, String orderId, String productCategory, PageRequest pr) {

        Page<ProductOrder> orderPage = productOrderRepository.fetchCustomerOrdersBy(productId, emailAddress, status, orderId, productCategory, pr);

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
    public PaginatedResponse<List<ProductOrderDto>> fetchVendorOrdersBy(String productId , String status, String orderId, String productCategory, PageRequest pr) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Page<ProductOrder> orderPage = productOrderRepository.fetchVendorOrdersBy(productId, username, status, orderId, productCategory, pr);

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
    public ProductOrderDto getProductOrder(String productOrderId){
        Optional<ProductOrder> existingProductOrder = productOrderRepository.findByProductId(productOrderId);
        if(existingProductOrder.isEmpty()){
            throw new StitchException("product order does not exist " + productOrderId);
        }
        ProductOrder productOrder = existingProductOrder.get();
        ProductOrderDto productOrderDto = new ProductOrderDto();
        return (ProductOrderDto) convertModelToDto(productOrder, productOrderDto);
    }

    @Override
    public ProductOrderDto getOrderByOrderId(String orderId){
        Optional<ProductOrder> existingProductOrder = productOrderRepository.findByOrderId(orderId);
        if(existingProductOrder.isEmpty()){
            throw new StitchException("product order does not exist " + orderId);
        }

        ProductOrder productOrder = existingProductOrder.get();
        BodyMeasurement bodyMeasurement = findBodyMeasurement(productOrder.getBodyMeasurementTag(), productOrder.getEmailAddress());
        BodyMeasurementDto bodyMeasurementDto = convertBodyMeasurementToModel(bodyMeasurement);

        ProductOrderDto productOrderDto = convertProductOrderToDto(existingProductOrder.get(), bodyMeasurementDto);
        log.info("productOrderDto :{}", productOrderDto);
        return productOrderDto;
    }

    public BodyMeasurement findBodyMeasurement(String id, String username){
        Optional<UserEntity> existingUser = userRepository.findByEmailAddress(username);
        if(existingUser.isEmpty()){
            throw new StitchException("user does not exist");
        }
        UserEntity userEntity = existingUser.get();
        Optional<BodyMeasurement> existingBodyMeasurement = bodyMeasurementRepository.findBodyMeasurementByTagAndUserEntity(id, userEntity);
        if(existingBodyMeasurement.isEmpty()){
            throw new StitchException("Body measurement does not exist");
        }
        return existingBodyMeasurement.get();
    }

    @Override
    public List<ProductOrder> getOrdersByTransactionId(String orderId){
        return productOrderRepository.findProductOrdersByTransactionId(orderId);
    }

    @Override
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ProductOrderDto createProductOrder(ProductOrderRequest productOrderDto){
        ProductOrder productOrder = convertRequestToModel(productOrderDto);
        ProductOrder savedproductOrder = productOrderRepository.save(productOrder);

        return convertProductOrderToDto(savedproductOrder);
    }

    @Override
    public ProductOrderDto updateProductOrder(ProductOrderRequest productOrderDto){
        log.info("productOrderReq : {}", productOrderDto.getOrderId());
        Optional<ProductOrder> existingProductOrder = productOrderRepository.findByOrderId(productOrderDto.getOrderId());
        if(existingProductOrder.isEmpty()){
            throw new StitchException("product order is not found");
        }
        ProductOrder order = existingProductOrder.get();
        log.info("order : {}", order);

        order.setStatus(OrderStatus.valueOf(productOrderDto.getStatus()));

        log.info("order 1: {}", order);

        ProductOrder savedproductOrder = productOrderRepository.save(order);
        return convertProductOrderToDto(savedproductOrder);
    }

    @Override
    public ProductOrderStatistics getCustomerProductStat(){
        String emailAddress = SecurityContextHolder.getContext().getAuthentication().getName();

        List<ProductOrder> existingProductOrder = productOrderRepository.findByEmailAddress(emailAddress);
        if(existingProductOrder.isEmpty()){
            throw new StitchException("customer with : " + emailAddress + " does not exist");
        }

        ProductOrderStatistics productOrderStatistics = new ProductOrderStatistics();
        productOrderStatistics.setAllOrdersCount(productOrderRepository.countAllOrdersByCustomerId(emailAddress));
        productOrderStatistics.setCompletedOrdersCount(productOrderRepository.countCompletedOrdersByCustomerId(emailAddress));
        productOrderStatistics.setCancelledOrdersCount(productOrderRepository.countCancelledOrdersByCustomerId(emailAddress));
        productOrderStatistics.setProcessingOrdersCount(productOrderRepository.countProcessingOrdersByCustomerId(emailAddress));
        productOrderStatistics.setFailedOrdersCount(productOrderRepository.countFailedOrdersByCustomerId(emailAddress));
        productOrderStatistics.setInTransitOrdersCount(productOrderRepository.countInTransitOrdersByCustomerId(emailAddress));
        productOrderStatistics.setPaymentCompletedCount(productOrderRepository.countPaymentCompletedOrdersByCustomerId(emailAddress));

        return productOrderStatistics;
    }

    @Override
    public ProductOrderStatistics getVendorProductStat(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<ProductOrder> existingProductOrder = productOrderRepository.findProductOrderByVendorEmailAddress(username);

        if(existingProductOrder.isEmpty()){
            throw new StitchException("product order is empty");
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
    }

}
