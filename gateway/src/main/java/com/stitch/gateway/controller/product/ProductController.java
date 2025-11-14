package com.stitch.gateway.controller.product;


import com.stitch.commons.model.dto.PaginatedResponse;
import com.stitch.commons.model.dto.Response;
import com.stitch.gateway.security.model.Unsecured;
import com.stitch.model.ProductCategory;
import com.stitch.model.dto.ProductDto;
import com.stitch.model.dto.ProductFilterRequest;
import com.stitch.model.dto.ProductRequest;
import com.stitch.model.dto.ProductUpdateRequest;
import com.stitch.model.enums.PublishStatus;
import com.stitch.service.ProductService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.stitch.gateway.util.Constants.BASE_URL;
import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@RequestMapping(BASE_URL)
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create-product")
    @PreAuthorize("hasAuthority('VENDOR')")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductRequest productRequest) {
        return new ResponseEntity<>(productService.createProduct(productRequest), CREATED);

    }

    @PatchMapping("/toggle-publish-product")
    @PreAuthorize("hasAuthority('VENDOR')")
    public ResponseEntity<Map<String, Boolean>> togglePublishProduct(@RequestParam(value = "productId") String productId) {
        return ResponseEntity.ok(Map.of("response", productService.togglePublishProduct(productId)));
    }

    @PutMapping("/update-product/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody() ProductUpdateRequest productRequest, @PathVariable("productId") String productId) {
        return ResponseEntity.ok(productService.updateProduct(productRequest, productId));
    }

    @DeleteMapping("/delete-product/{productId}")
    @PreAuthorize("hasAuthority('VENDOR')")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable(value = "productId") String productId) {
        return ResponseEntity.ok(productService.deleteProduct(productId));
    }

    @Unsecured
    @GetMapping("/get-product-by-id")
    public ResponseEntity<ProductDto> getProductByProductId(@RequestParam(value = "productId") String productId) {
        return ResponseEntity.ok(productService.getProductByProductId(productId));
    }

    @GetMapping("/get-products-by-vendor")
    @PreAuthorize("hasAuthority('VENDOR')")
    public ResponseEntity<PaginatedResponse<List<ProductDto>>> getProductsByVendorId(@RequestParam("vendorId") String vendorId,
                                                                                     @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(productService.getProductByVendor(vendorId, page, size));

    }

    @PostMapping("/update-product-profile-image")
    @PreAuthorize("hasAuthority('VENDOR')")
    public ResponseEntity<Response> updateProductProfileImage(@RequestParam("productImage") String productImage, @RequestParam("productId") String productId) {
        return ResponseEntity.ok(productService.updateProductProfileImage(productImage, productId));

    }

    @GetMapping("/get-vendor-products")
    @PreAuthorize("hasAuthority('VENDOR')")
    public ResponseEntity<PaginatedResponse<List<ProductDto>>> getVendorProductsBy(
            @RequestBody @Valid ProductFilterRequest productFilterRequest) {
        return ResponseEntity.ok(productService.fetchAllProductsByVendor(productFilterRequest));
    }

//    @Unsecured
//    @GetMapping("/get-all-products")
//    public ResponseEntity<PaginatedResponse<List<ProductDto>>> getAllProductsBy(
//            @RequestBody ProductFilterRequest productFilterRequest) {
//        return ResponseEntity.ok(productService.fetchAllProductsBy(productFilterRequest));
//    }

    @Unsecured
    @GetMapping("/get-all-products")
    public ResponseEntity<PaginatedResponse<List<ProductDto>>> getAllProductsBy(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) Boolean outOfStock,
            @RequestParam(required = false) List<ProductCategory> categories,
            @RequestParam(required = false) String provider,
            @RequestParam(required = false) String vendorId,
            @RequestParam(required = false) PublishStatus publishStatus,
            @RequestParam(required = false) String productId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {

        ProductFilterRequest filterRequest = ProductFilterRequest.builder()
                .page(page)
                .size(size)
                .name(name)
                .code(code)
                .outOfStock(outOfStock != null ? outOfStock : false)
                .categories(categories)
                .provider(provider)
                .vendorId(vendorId)
                .publishStatus(publishStatus)
                .productId(productId)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .build();

        return ResponseEntity.ok(productService.fetchAllProductsBy(filterRequest));
    }

//    @GetMapping("/get-all-products-by-auth")
//    public ResponseEntity<PaginatedResponse<List<ProductDto>>> getAllProductsByAuth(
//            @RequestBody ProductFilterRequest productFilterRequest) {
//        return ResponseEntity.ok(productService.fetchAllProductsByAuth(productFilterRequest));
//    }

    @GetMapping("/get-all-products-by-auth")
    public ResponseEntity<PaginatedResponse<List<ProductDto>>> getAllProductsByAuth(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) Boolean outOfStock,
            @RequestParam(required = false) List<ProductCategory> categories,
            @RequestParam(required = false) String provider,
            @RequestParam(required = false) String vendorId,
            @RequestParam(required = false) PublishStatus publishStatus,
            @RequestParam(required = false) String productId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {

        ProductFilterRequest filterRequest = ProductFilterRequest.builder()
                .page(page)
                .size(size)
                .name(name)
                .code(code)
                .outOfStock(outOfStock != null ? outOfStock : false)
                .categories(categories)
                .provider(provider)
                .vendorId(vendorId)
                .publishStatus(publishStatus)
                .productId(productId)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .build();
        return ResponseEntity.ok(productService.fetchAllProductsByAuth(filterRequest));
    }
}

