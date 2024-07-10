package com.stitch.gateway.controller.productcart;

import com.stitch.commons.exception.StitchException;
import com.stitch.commons.model.dto.PaginatedResponse;
import com.stitch.commons.model.dto.Response;
import com.stitch.model.dto.CartDto;
import com.stitch.model.dto.PageRequest;
import com.stitch.payment.model.dto.PaymentVerificationResponse;
import com.stitch.payment.model.entity.InitializeTransactionRequest;
import com.stitch.payment.model.entity.InitializeTransactionResponse;
import com.stitch.payment.service.PaymentService;
import com.stitch.service.ProductCartService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class ProductCartController {
    private final ProductCartService productCartService;
    private final PaymentService paymentService;

    public ProductCartController(ProductCartService productCartService, PaymentService paymentService) {
        this.productCartService = productCartService;
        this.paymentService = paymentService;
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
            return productCartService.removeOrReduceFromCart(productId);
        }catch (StitchException e){
            throw new StitchException(e.getMessage());
        }
    }

    @MutationMapping(value = "removeEntireProductFromCart")
    public Response removeEntireProductFromCart(@Argument("productId")String productId){
        try {
            return productCartService.removeProductFromCart(productId);
        }catch (StitchException e){
            throw new StitchException(e.getMessage());
        }
    }

    @MutationMapping(value = "clearCart")
    public Response clearCart(){
        try {
            return productCartService.clearCart();
        }catch (StitchException e){
            throw new StitchException(e.getMessage());
        }
    }



    @QueryMapping(value = "getCart")
    public PaginatedResponse<List<CartDto>> getCart(@Argument("pageRequest") PageRequest pageRequest){
        try {
            return productCartService.getCart(pageRequest.getPage(), pageRequest.getSize());
        }catch (StitchException e){
            throw new StitchException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @QueryMapping(value = "sumAmountByQuantityByCustomerId")
    public BigDecimal sumAmountByQuantityByCustomerId(){
        try {
            return productCartService.sumAmountByQuantityByCustomerId();
        }catch (StitchException e){
            throw new StitchException(e.getMessage());
        }
    }

}
