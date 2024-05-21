package com.stitch.service;

import com.stitch.commons.model.dto.PaginatedResponse;
import com.stitch.commons.model.dto.Response;
import com.stitch.model.dto.ProductDto;

import java.util.List;

public interface ProductLikeService {
    Response addToLikes(String productId);

    Response removeFromLikes(String productId);

    PaginatedResponse<List<ProductDto>> getAllLikes(int page, int size);
}
