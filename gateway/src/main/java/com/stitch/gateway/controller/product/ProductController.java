package com.stitch.gateway.controller.product;


import com.stitch.commons.exception.StitchException;
import com.stitch.commons.model.dto.PaginatedResponse;
import com.stitch.commons.model.dto.Response;
import com.stitch.gateway.security.model.Unsecured;
import com.stitch.model.dto.ProductDto;
import com.stitch.model.dto.ProductFilterRequest;
import com.stitch.model.dto.ProductRequest;
import com.stitch.model.dto.ProductUpdateRequest;
import com.stitch.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class ProductController {

    private final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @MutationMapping(value = "createProduct")
    public ProductDto createProduct(@Argument("productRequest")ProductRequest productRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        log.info("Authorities: {}", authorities);

        try {
            return productService.createProduct(productRequest);
        }catch (StitchException e){
            throw new StitchException(e.getMessage());
        }
    }

    @MutationMapping(value = "togglePublishProduct")
    public boolean togglePublishProduct(@Argument("productId")String productId){
        try {
            return productService.togglePublishProduct(productId);
        }catch (StitchException e){
            throw new StitchException(e.getMessage());
        }
    }

    @MutationMapping(value = "updateProduct")
    public ProductDto updateProduct(@Argument("productUpdateRequest") ProductUpdateRequest productRequest, @Argument("productId") String productId){
        try {
            return productService.updateProduct(productRequest, productId);
        }catch (StitchException e){
            throw new StitchException(e.getMessage());
        }
    }

    @MutationMapping(value = "deleteProduct")
    public void deleteProduct(@Argument("productId") String productId){
        try {
             productService.deleteProduct(productId);
        }catch (StitchException e){
            throw new StitchException(e.getMessage());
        }
    }

    @Unsecured
    @QueryMapping(value = "getProductByProductId")
    public ProductDto getProductByProductId(@Argument("productId") String productId){
        try {
            return productService.getProductByProductId(productId);
        }catch (StitchException e){
            throw new StitchException(e.getMessage());
        }
    }

    @QueryMapping(value = "getProductsByVendorId")
    public PaginatedResponse<List<ProductDto>> getProductsByVendorId(@Argument("vendorId") String vendorId,
                                                                  @Argument("page") Optional<Integer> page,
                                                                  @Argument("size") Optional<Integer> size
    ){
        PageRequest pr = PageRequest.of(page.orElse(0), size.orElse(10));

        try {
            return productService.getProductByVendor(vendorId, pr);
        }catch (StitchException e){
            throw new StitchException(e.getMessage());
        }
    }

    @MutationMapping(value = "updateProductProfileImage")
    public Response updateProductProfileImage(@Argument("productImage") String productImage, @Argument("productId") String productId){
        try {
            return productService.updateProductProfileImage(productImage, productId);
        }catch (StitchException e){
            throw new StitchException(e.getMessage());
        }
    }

    @QueryMapping(value = "getVendorProductsBy")
    public PaginatedResponse<List<ProductDto>> getVendorProductsBy(
            @Argument("productFilterRequest") ProductFilterRequest productFilterRequest
    ) {
        return productService.fetchAllProductsByVendor(productFilterRequest);
    }

    @Unsecured
    @QueryMapping(value = "getAllProductsBy")
    public PaginatedResponse<List<ProductDto>> getAllProductsBy(
            @Argument("productFilterRequest") ProductFilterRequest productFilterRequest
    ) {
        return productService.fetchAllProductsBy(productFilterRequest);
    }

    @QueryMapping(value = "getAllProductsByAuth")
    public PaginatedResponse<List<ProductDto>> getAllProductsByAuth(
            @Argument("productFilterRequest") ProductFilterRequest productFilterRequest
    ) {
        return productService.fetchAllProductsByAuth(productFilterRequest);
    }
}
