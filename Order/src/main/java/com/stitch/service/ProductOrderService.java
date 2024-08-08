package com.stitch.service;

import com.stitch.commons.model.dto.PaginatedResponse;
import com.stitch.model.dto.ProductOrderDto;
import com.stitch.model.dto.ProductOrderRequest;
import com.stitch.model.dto.ProductOrderStatistics;
import com.stitch.model.entity.ProductOrder;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ProductOrderService {

    PaginatedResponse<List<ProductOrderDto>> fetchCustomerOrdersBy(String productId, String userId, String status, String orderId, String productCategory, PageRequest pr);

    PaginatedResponse<List<ProductOrderDto>> fetchVendorOrdersBy(String productId, String status, String orderId, String productCategory, PageRequest pr);


    ProductOrderDto getProductOrder(String productOrderId);

    ProductOrderDto getOrderByOrderId(String productOrderId);

    List<ProductOrder> getOrdersByTransactionId(String orderId);

    ProductOrderDto createProductOrder(ProductOrderRequest productOrderDto);


    ProductOrderDto updateProductOrder(ProductOrderRequest productOrderDto);

    ProductOrderStatistics getCustomerProductStat(String customerId);

    ProductOrderStatistics getVendorProductStat();
}
