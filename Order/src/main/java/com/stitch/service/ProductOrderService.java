package com.stitch.service;

import com.stitch.commons.model.dto.PaginatedResponse;
import com.stitch.model.dto.ProductOrderDto;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ProductOrderService {

    PaginatedResponse<List<ProductOrderDto>> fetchCustomerOrdersBy(String productId, String status, String orderId, String productCategory, String vendorId, PageRequest pr);
}
