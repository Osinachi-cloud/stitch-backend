package com.stitch.gateway.controller.productcart;

import com.stitch.commons.exception.StitchException;
import com.stitch.commons.model.dto.PaginatedResponse;
import com.stitch.commons.model.dto.Response;
import com.stitch.model.dto.ProductDto;
import com.stitch.model.dto.ProductLikeRequest;
import com.stitch.service.ProductCartService;
import com.stitch.service.ProductLikeService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ProductCartController {
    private final ProductCartService productCartService;

    public ProductCartController(ProductCartService productCartService) {
        this.productCartService = productCartService;
    }

    @MutationMapping(value = "addProductCart")
    public Response addProductLikes(@Argument("productId") String productId){
        try {
            return productCartService.addToCart(productId);
        }catch (StitchException e){
            throw new StitchException(e.getMessage());
        }
    }

    @MutationMapping(value = "deleteProductCart")
    public Response deleteProductcart(@Argument("productId")String productId){
        try {
            return productCartService.removeFromCart(productId);
        }catch (StitchException e){
            throw new StitchException(e.getMessage());
        }
    }

    @QueryMapping(value = "getAllProductCart")
    public PaginatedResponse<List<ProductDto>> getAllProductCart(@Argument("productCartRequest") ProductLikeRequest productCartRequest){
        try {
            return productCartService.getCart(productCartRequest.getPage(), productCartRequest.getSize());
        }catch (StitchException e){
            throw new StitchException(e.getMessage());
        }
    }

}
