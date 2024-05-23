package com.stitch.service.serviceImpl;

import com.stitch.commons.exception.StitchException;
import com.stitch.commons.model.dto.PaginatedResponse;
import com.stitch.commons.model.dto.Response;
import com.stitch.commons.util.ResponseUtils;
import com.stitch.model.dto.CartDto;
import com.stitch.model.dto.ProductDto;
import com.stitch.model.entity.Product;
import com.stitch.model.entity.ProductCart;
import com.stitch.repository.ProductCartRepository;
import com.stitch.repository.ProductRepository;
import com.stitch.service.ProductCartService;
import com.stitch.user.model.entity.Customer;
import com.stitch.user.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class ProductCartServiceImpl implements ProductCartService{

    private final ProductCartRepository productCartRepository;
    private final ProductRepository productRepository;

    private final CustomerRepository customerRepository;

    public ProductCartServiceImpl(ProductCartRepository productCartRepository, ProductRepository productRepository, CustomerRepository customerRepository) {
        this.productCartRepository = productCartRepository;
        this.productRepository = productRepository;

        this.customerRepository = customerRepository;
    }

    @Override
    public Response addToCart(String productId){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<Customer> customerOptional = customerRepository.findByEmailAddress(username);
        if(customerOptional.isEmpty()){
            throw new StitchException("Customer with Id : " + username + " does not exist");
        }

        Customer customer = customerOptional.get();

        Optional<Product> existingProduct = productRepository.findByProductId(productId);

        if(existingProduct.isEmpty()){
            throw new StitchException("Product with Id : " + productId + " does not exist");
        }

        Optional<ProductCart> existingProductCart = productCartRepository.findByProductId(productId);
        if(existingProductCart.isPresent()){
            ProductCart productCart = existingProductCart.get();
            productCart.setQuantity(productCart.getQuantity() + 1);
            productCart.setAmountByQuantity(existingProduct.get().getSellingPrice().multiply(BigDecimal.valueOf(productCart.getQuantity())));
            productCartRepository.save(productCart);
            return ResponseUtils.createDefaultSuccessResponse();

        }else {
            ProductCart productCart = new ProductCart();
            productCart.setProductId(productId);
            productCart.setCustomer(customer);
            productCart.setQuantity(1);

            productCartRepository.save(productCart);
            return ResponseUtils.createDefaultSuccessResponse();

        }
    }

    @Override
    public Response removeOrReduceFromCart(String productId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<Product> existingProduct = productRepository.findByProductId(productId);

        if(existingProduct.isEmpty()){
            throw new StitchException("Product with Id : " + productId + " does not exist");
        }

        Optional<ProductCart> existingProductCart = productCartRepository.findByProductId(productId);
        if(existingProductCart.isEmpty()){
            throw new StitchException("Product in cart with Id : " + productId + " does not exist");
        }else if(existingProductCart.get().getQuantity() > 1){
            ProductCart productCart = existingProductCart.get();
            productCart.setQuantity(productCart.getQuantity() - 1);
            productCart.setAmountByQuantity(existingProduct.get().getSellingPrice().multiply(BigDecimal.valueOf(productCart.getQuantity())));
            productCartRepository.save(productCart);
        }else {
            productCartRepository.delete(existingProductCart.get());
        }
        return ResponseUtils.createDefaultSuccessResponse();
    }

    @Override
    public Response removeProductFromCart(String productId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<ProductCart> existingProductCart = productCartRepository.findByProductId(productId);
        if(existingProductCart.isEmpty()){
            throw new StitchException("Product in cart with Id : " + productId + " does not exist");
        }
        productCartRepository.delete(existingProductCart.get());
        return ResponseUtils.createDefaultSuccessResponse();
    }


    @Override
    public PaginatedResponse<List<CartDto>> getCart(int page, int size){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<Customer> customerOptional = customerRepository.findByEmailAddress(username);
        if(customerOptional.isEmpty()){
            throw new StitchException("Customer with Id : " + username + " does not exist");
        }
        Customer customer = customerOptional.get();

        Pageable pagerequest = PageRequest.of(page, size);

        log.info("customer id : {}", customer.getCustomerId());

        Page<ProductCart> productCart = productCartRepository.findProductCartByCustomer(customer, pagerequest);

        log.info("productCart : {}", productCart.getContent());


        PaginatedResponse<List<CartDto>> paginatedResponse = new PaginatedResponse<>();
        paginatedResponse.setPage(productCart.getNumber());
        paginatedResponse.setSize(productCart.getSize());
        paginatedResponse.setTotal((int) productCartRepository.getCartCount(customer.getCustomerId()));
        paginatedResponse.setData(convertProductCartListToDto(productCart.getContent()));
        return paginatedResponse;
    }


    private List<CartDto> convertProductCartListToDto(List<ProductCart> productCartList){

        List<CartDto> productDtoList = new ArrayList<>();
        for(ProductCart productCart: productCartList){
            Optional<Product> productExists = productRepository.findByProductId(productCart.getProductId());
            if(productExists.isEmpty()){
                throw new StitchException("Product with : " + productCart.getProductId()  + " does not exist");
            }
            Product product = productExists.get();

            CartDto cartDto = new CartDto();
            BeanUtils.copyProperties(product, cartDto);
            cartDto.setAmountByQuantity(productCart.getAmountByQuantity());
            cartDto.setQuantity(BigDecimal.valueOf(productCart.getQuantity()));

            productDtoList.add(cartDto);
        }
        return productDtoList;
    }
}
