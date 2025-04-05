package com.stitch.gateway.controller.productLike;

import com.stitch.commons.model.dto.PaginatedResponse;
import com.stitch.commons.model.dto.Response;
import com.stitch.gateway.model.dto.PageRequest;
import com.stitch.model.dto.*;
import com.stitch.service.ProductLikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.stitch.gateway.util.Constants.BASE_URL;

@RestController
@RequestMapping(BASE_URL)
public class ProductLikeController {
    private final ProductLikeService productLikeService;

    public ProductLikeController(ProductLikeService productLikeService) {
        this.productLikeService = productLikeService;
    }

    @PostMapping("/add-product-likes/{productId}")
    public ResponseEntity<Response> addProductLikes(@PathVariable String productId) {
        return ResponseEntity.ok(productLikeService.addToLikes(productId));
    }

    @DeleteMapping("/delete-product-like/{productId}")
    public ResponseEntity<Response> deleteProductLike(@PathVariable String productId) {
        return ResponseEntity.ok(productLikeService.removeFromLikes(productId));
    }

    @GetMapping("/get-all-product-likes")
    public ResponseEntity<PaginatedResponse<List<ProductDto>>> getAllProductLikes(@RequestBody PageRequest pageRequest) {
        return ResponseEntity.ok(productLikeService.getAllLikes(pageRequest.getPage(), pageRequest.getSize()));
    }

}
