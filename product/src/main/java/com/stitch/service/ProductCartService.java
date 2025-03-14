package com.stitch.service;

import com.stitch.commons.model.dto.PaginatedResponse;
import com.stitch.commons.model.dto.Response;
import com.stitch.model.dto.CartDto;
import com.stitch.model.dto.ProductVariationRequest;

import java.math.BigDecimal;
import java.util.List;

public interface ProductCartService {
    Response addToCart(String productId, ProductVariationRequest productVariationDto);
    Response addToCart(String productId);

    Response removeOrReduceFromCart(String productId, ProductVariationRequest productVariationDto);

    Response removeProductFromCart(String productId, ProductVariationRequest productVariationDto);

    PaginatedResponse<List<CartDto>> getCart(int page, int size);

    BigDecimal sumAmountByQuantityByCustomerId();

    Response clearCart();
}

