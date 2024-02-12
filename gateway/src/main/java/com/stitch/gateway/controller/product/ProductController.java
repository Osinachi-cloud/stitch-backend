package com.stitch.gateway.controller.product;


import com.stitch.commons.exception.StitchException;
import com.stitch.commons.model.dto.PaginatedResponse;
import com.stitch.commons.model.dto.Response;
import com.stitch.model.dto.ProductDto;
import com.stitch.model.dto.ProductRequest;
import com.stitch.model.dto.ProductUpdateRequest;
import com.stitch.service.ProductService;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @MutationMapping(value = "createProduct")
    public ProductDto createProduct(@Argument("productRequest")ProductRequest productRequest){
        try {
            return productService.createProduct(productRequest);
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

    @QueryMapping(value = "getProductByProductId")
    public ProductDto getProductByProductId(@Argument("productId") String productId){
        try {
            return productService.getProductByProductId(productId);
        }catch (StitchException e){
            throw new StitchException(e.getMessage());
        }
    }

    @QueryMapping(value = "getProductByVendor")
    public PaginatedResponse<List<ProductDto>> getProductByVendor(@Argument("productId") String vendorId,
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



//    @QueryMapping(value = "getAllProductCategories")
//    public List<ProductCategoryDto> getProductCategories() {
//        return productService.getAllProductCategories();
//    }

//    @QueryMapping(value = "getActiveProductCategories")
//    public List<ProductCategoryDto> getActiveProductCategories() {
//        return productService.getActiveProductCategories();
//    }
//
//    @QueryMapping(value = "getAllProviders")
//    public List<ProviderDto> getProviders(@Argument("categoryName") String categoryName) {
//        return productService.getAllProvidersByCategoryName(categoryName);
//    }
//
//    @QueryMapping(value = "getActiveProviders")
//    public List<ProviderDto> getActiveProviders(@Argument("categoryName") String categoryName) {
//        return productService.getActiveProvidersByCategoryName(categoryName);
//    }

}
