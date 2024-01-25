package com.stitch.service.serviceImpl;

import com.stitch.commons.model.dto.Response;
import com.stitch.model.dto.ProductDto;
import com.stitch.model.dto.ProductRequest;
import com.stitch.model.dto.ProductUpdateRequest;
import com.stitch.service.ProductService;
import org.springframework.data.domain.Page;

public class ProductServiceImpl implements ProductService {

    @Override
    public ProductDto createProduct(ProductRequest productRequest) {
        return null;
    }

    @Override
    public ProductDto updateProduct(ProductUpdateRequest productRequest, String emailAddress) {
        return null;
    }

    @Override
    public Response updateProductProfileImage(String profileImage, String emailAddress) {
        return null;
    }

    @Override
    public ProductDto getProductByProductId(String productId) {
        return null;
    }

    @Override
    public Page<ProductDto> getProductByVendor(String vendorId) {
        return null;
    }

    @Override
    public void deleteProduct(String productId) {

    }


}
