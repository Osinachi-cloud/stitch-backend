package com.stitch.service;

import com.stitch.commons.model.dto.PaginatedResponse;
import com.stitch.commons.model.dto.Response;
import com.stitch.model.dto.ProductDto;
import com.stitch.model.dto.ProductFilterRequest;
import com.stitch.model.dto.ProductRequest;
import com.stitch.model.dto.ProductUpdateRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    ProductDto createProduct(ProductRequest productRequest);

    ProductDto updateProduct(ProductUpdateRequest productRequest, String productId);

    Response updateProductProfileImage(String profileImage, String productId);

    ProductDto getProductByProductId(String productId);

    PaginatedResponse<List<ProductDto>> getProductByVendor(String vendorId, Pageable pageable) ;

    void deleteProduct(String productId);


    PaginatedResponse<List<ProductDto>> fetchAllProductsByVendor(ProductFilterRequest request);

    PaginatedResponse<List<ProductDto>> fetchAllProductsBy(ProductFilterRequest request);

    PaginatedResponse<List<ProductDto>> fetchAllProductsByAuth(ProductFilterRequest request);

    boolean togglePublishProduct(String productId);
}
