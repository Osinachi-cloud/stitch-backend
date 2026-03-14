package com.stitch.gateway.controller.productcart;

import com.stitch.commons.model.dto.PaginatedResponse;
import com.stitch.commons.model.dto.Response;
import com.stitch.model.dto.CartDto;
import com.stitch.gateway.model.dto.PageRequest;
import com.stitch.model.dto.ProductVariationRequest;
import com.stitch.service.ProductCartService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.stitch.gateway.util.Constants.BASE_URL;

@Slf4j
@RestController
@RequestMapping(BASE_URL)
public class ProductCartController {
    private final ProductCartService productCartService;


    public ProductCartController(ProductCartService productCartService) {
        this.productCartService = productCartService;
    }

    @PostMapping("/add-to-cart")
    public ResponseEntity<Response> addProductCart(@RequestParam("productId") String productId) {
        return ResponseEntity.ok(productCartService.addToCart(productId));
    }

    @PostMapping("/add-product-cart-with-variation")
    public ResponseEntity<Response> addProductCartWithVariation(@RequestParam("productId") String productId,  @RequestBody ProductVariationRequest productVariationDto) {
        return ResponseEntity.ok(productCartService.addToCart(productId, productVariationDto));
    }

    @PostMapping("/increase-cart-with-variation")
    public ResponseEntity<Response> increaseProductCartWithVariation(@RequestParam("productId") String productId, @RequestParam("quantity") Optional<Integer> quantity,  @RequestBody ProductVariationRequest productVariationDto) {
        return ResponseEntity.ok(productCartService.increaseToCart(productId, quantity.orElse(1), productVariationDto));
    }

    @PutMapping("/delete-product-cart")
    public ResponseEntity<Response> deleteProductCart(@RequestParam(value = "productId", required = false) String productId, @RequestBody ProductVariationRequest productVariationDto) {
        return ResponseEntity.ok(productCartService.removeOrReduceFromCart(productId, productVariationDto));
    }

    @DeleteMapping("/remove-all-product-from-cart")
    public ResponseEntity<Response> removeEntireProductFromCart(@RequestParam("productId") String productId, @RequestBody ProductVariationRequest productVariationDto) {
        return ResponseEntity.ok(productCartService.removeProductFromCart(productId, productVariationDto));
    }

    @PutMapping("/clear-cart")
    public ResponseEntity<Response> clearCart() {
        return ResponseEntity.ok(productCartService.clearCart());

    }

    @GetMapping("/get-cart")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<PaginatedResponse<List<CartDto>>> getCart(@RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(page.orElse(0));
        pageRequest.setSize(size.orElse(20));
        return ResponseEntity.ok(productCartService.getCart(pageRequest.getPage(), pageRequest.getSize()));
    }

    @GetMapping("/sum-amount-by-quantity-by-customerId")
    public ResponseEntity<Map<String, BigDecimal>> sumAmountByQuantityByCustomerId() {
        return ResponseEntity.ok(productCartService.sumAmountByQuantityByCustomerId());
    }

}
