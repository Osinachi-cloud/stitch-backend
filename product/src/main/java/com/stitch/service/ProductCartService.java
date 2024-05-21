package com.stitch.service;

import com.stitch.commons.model.dto.PaginatedResponse;
import com.stitch.commons.model.dto.Response;
import com.stitch.model.dto.ProductDto;

import java.util.List;

public interface ProductCartService {
    Response addToCart(String productId);

    Response removeFromCart(String productId);

    PaginatedResponse<List<ProductDto>> getCart(int page, int size);
}

