package com.stitch.service;

import com.stitch.commons.model.dto.Response;
import com.stitch.model.dto.ProductDto;
import com.stitch.model.dto.ProductRequest;
import com.stitch.model.dto.ProductUpdateRequest;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;
import java.util.List;

public interface ProductService {

    ProductDto createProduct(ProductRequest productRequest);

    ProductDto updateProduct(ProductUpdateRequest productRequest, String emailAddress);

    Response updateProductProfileImage(String profileImage, String emailAddress);

    ProductDto getProductByProductId(String productId);

    Page<ProductDto> getProductByVendor(String vendorId);

    void deleteProduct(String productId);


}
