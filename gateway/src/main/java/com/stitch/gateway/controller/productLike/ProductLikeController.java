package com.stitch.gateway.controller.productLike;

import com.stitch.commons.exception.StitchException;
import com.stitch.commons.model.dto.PaginatedResponse;
import com.stitch.commons.model.dto.Response;
import com.stitch.model.dto.*;
import com.stitch.service.ProductLikeService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ProductLikeController {
    private final ProductLikeService productLikeService;

    public ProductLikeController(ProductLikeService productLikeService) {
        this.productLikeService = productLikeService;
    }

    @MutationMapping(value = "addProductLikes")
    public Response addProductLikes(@Argument("productId") String productId){
        try {
            return productLikeService.addToLikes(productId);
        }catch (StitchException e){
            throw new StitchException(e.getMessage());
        }
    }

    @MutationMapping(value = "deleteProductLike")
    public Response deleteProductLike(@Argument("productId")String productId){
        try {
            return productLikeService.removeFromLikes(productId);
        }catch (StitchException e){
            throw new StitchException(e.getMessage());
        }
    }

    @QueryMapping(value = "getAllProductLikes")
    public PaginatedResponse<List<ProductDto>> getAllProductLikes(@Argument("productLikeRequest") ProductLikeRequest productLikeRequest){
        try {
            return productLikeService.getAllLikes(productLikeRequest.getPage(), productLikeRequest.getSize());
        }catch (StitchException e){
            throw new StitchException(e.getMessage());
        }
    }

}
