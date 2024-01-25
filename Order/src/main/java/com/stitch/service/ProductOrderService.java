package com.stitch.service;

import com.stitch.commons.model.dto.PaginatedResponse;
import com.stitch.model.dto.ProductOrderDto;
import com.stitch.model.dto.ProductOrderRequest;
import com.stitch.model.dto.ProductOrderStatistics;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ProductOrderService {

    PaginatedResponse<List<ProductOrderDto>> fetchCustomerOrdersBy(String productId, String customerId, String status, String orderId, String productCategory, String vendorId, PageRequest pr);

    ProductOrderDto getProductOrder(String productOrderId);

    ProductOrderDto createProductOrder(ProductOrderRequest productOrderDto);


    ProductOrderStatistics getCustomerProductStat(String customerId);
}
