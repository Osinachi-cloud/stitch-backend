package com.stitch.service.serviceImpl;

import com.stitch.commons.exception.StitchException;
import com.stitch.commons.model.dto.PaginatedResponse;
import com.stitch.commons.model.dto.Response;
import com.stitch.commons.util.ResponseUtils;
import com.stitch.model.dto.CartDto;
import com.stitch.model.dto.ProductVariationRequest;
import com.stitch.model.entity.Product;
import com.stitch.model.entity.ProductCart;
import com.stitch.repository.ProductCartRepository;
import com.stitch.repository.ProductRepository;
import com.stitch.service.ProductCartService;
import com.stitch.user.model.entity.UserEntity;
import com.stitch.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class ProductCartServiceImpl implements ProductCartService{

    private final ProductCartRepository productCartRepository;
    private final ProductRepository productRepository;

    private final UserRepository customerRepository;

    public ProductCartServiceImpl(ProductCartRepository productCartRepository, ProductRepository productRepository, UserRepository customerRepository) {
        this.productCartRepository = productCartRepository;
        this.productRepository = productRepository;

        this.customerRepository = customerRepository;
    }

    @Override
    public Response addToCart(String productId){
        log.info("productId : {}", productId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<UserEntity> customerOptional = customerRepository.findByEmailAddress(username);
        if(customerOptional.isEmpty()){
            throw new StitchException("Customer with Id : " + username + " does not exist");
        }

        UserEntity customer = customerOptional.get();

        Optional<Product> existingProduct = productRepository.findByProductId(productId);

        if(existingProduct.isEmpty()){
            throw new StitchException("Product with Id : " + productId + " does not exist");
        }

        Optional<ProductCart> existingProductCart = productCartRepository.findByProductId(productId);
        if(existingProductCart.isPresent()){
            log.info("existingProductCart is present : {}", existingProductCart.get());
            ProductCart productCart = existingProductCart.get();
            productCart.setQuantity(productCart.getQuantity() + 1);
            productCart.setProductCategoryName(existingProduct.get().getCategory().name());
            productCart.setVendor(existingProduct.get().getVendor());
            productCart.setAmountByQuantity(existingProduct.get().getAmount().multiply(BigDecimal.valueOf(productCart.getQuantity())));
            productCartRepository.save(productCart);
            return ResponseUtils.createDefaultSuccessResponse();

        }else {
            log.info("existingProductCart is not present");
            ProductCart productCart = new ProductCart();
            productCart.setProductId(productId);
            productCart.setCustomer(customer);
            productCart.setQuantity(1);
            productCart.setAmountByQuantity(existingProduct.get().getAmount().multiply(BigDecimal.valueOf(productCart.getQuantity())));

            productCartRepository.save(productCart);
            return ResponseUtils.createDefaultSuccessResponse();
        }
    }

    @Override
    public Response addToCart(String productId, ProductVariationRequest productVariationDto){
        log.info("productId cart: {}", productId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<UserEntity> customerOptional = customerRepository.findByEmailAddress(username);
        if(customerOptional.isEmpty()){
            throw new StitchException("Customer with Id : " + username + " does not exist");
        }

        UserEntity customer = customerOptional.get();

        Optional<Product> existingProduct = productRepository.findByProductId(productId);

        if(existingProduct.isEmpty()){
            throw new StitchException("Product with Id : " + productId + " does not exist");
        }

        log.info("existingProduct: {}", existingProduct.get());


        Optional<ProductCart> existingProductCart = productCartRepository.findByProductIdAndColorAndSleeveTypeAndMeasurementTag(productId, productVariationDto.getColor(), productVariationDto.getSleeveType(), productVariationDto.getMeasurementTag());
        existingProductCart.ifPresent(productCart -> log.info("existingProductCart: {}", productCart));

        if(existingProductCart.isPresent()){
            log.info("existingProductCart is present : {}", existingProductCart.get());
            ProductCart productCart = existingProductCart.get();
            productCart.setQuantity(productCart.getQuantity() + 1);
            productCart.setProductCategoryName(existingProduct.get().getCategory().name());
            productCart.setVendor(existingProduct.get().getVendor());
            productCart.setAmountByQuantity(existingProduct.get().getAmount().multiply(BigDecimal.valueOf(productCart.getQuantity())));
            productCartRepository.save(productCart);
            return ResponseUtils.createDefaultSuccessResponse();

        }else {
            log.info("existingProductCart is not present");
            ProductCart productCart = new ProductCart();
            productCart.setProductId(productId);
            productCart.setColor(productVariationDto.getColor());
            productCart.setSleeveType(productVariationDto.getSleeveType());
            productCart.setMeasurementTag(productVariationDto.getMeasurementTag());
            productCart.setCustomer(customer);
            productCart.setQuantity(1);
            productCart.setAmountByQuantity(existingProduct.get().getAmount().multiply(BigDecimal.valueOf(productCart.getQuantity())));

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
            productCart.setAmountByQuantity(existingProduct.get().getAmount().multiply(BigDecimal.valueOf(productCart.getQuantity())));
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
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public PaginatedResponse<List<CartDto>> getCart(int page, int size){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<UserEntity> customerOptional = customerRepository.findByEmailAddress(username);
        if(customerOptional.isEmpty()){
            throw new StitchException("Customer with Id : " + username + " does not exist");
        }
        UserEntity customer = customerOptional.get();

        Pageable pageRequest = PageRequest.of(page, size);

        log.info("customer id : {}", customer.getUserId());
//
        Page<ProductCart> productCart = productCartRepository.findProductCartByCustomer(customer, pageRequest);

        log.info("productCart : {}", productCart.getContent());

        PaginatedResponse<List<CartDto>> paginatedResponse = new PaginatedResponse<>();
        paginatedResponse.setPage(productCart.getNumber());
        paginatedResponse.setSize(productCart.getSize());
        paginatedResponse.setTotal((int) productCartRepository.getCartCount(customer.getEmailAddress()));
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
            cartDto.setColor(productCart.getColor());
            cartDto.setMeasurementTag(productCart.getMeasurementTag());
            cartDto.setSleeveType(productCart.getSleeveType());
            cartDto.setVendorId(product.getVendor().getEmailAddress());

            productDtoList.add(cartDto);
        }
        return productDtoList;
    }

    @Override
    public BigDecimal sumAmountByQuantityByCustomerId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<UserEntity> customerOptional = customerRepository.findByEmailAddress(username);
        if(customerOptional.isEmpty()){
            throw new StitchException("Customer with Id : " + username + " does not exist");
        }
        UserEntity customer = customerOptional.get();

       return productCartRepository.sumAmountByQuantityByUserId(customer.getEmailAddress());
    }

    @Override
    @Transactional
    public Response clearCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<UserEntity> customerOptional = customerRepository.findByEmailAddress(username);
        if(customerOptional.isEmpty()){
            throw new StitchException("Customer with Id : " + username + " does not exist");
        }
        UserEntity customer = customerOptional.get();

        log.info("customer id : {}", customer.getUserId());

        List<ProductCart> productCart = productCartRepository.findProductCartByCustomer(customer);

        log.info("productCart : {}", productCart);

        for(ProductCart product: productCart){
            productCartRepository.delete(product);
        }

        return ResponseUtils.createDefaultSuccessResponse();
    }

//    @Override
    @Transactional
    public Response moveCartToOrder() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<UserEntity> customerOptional = customerRepository.findByEmailAddress(username);
        if(customerOptional.isEmpty()){
            throw new StitchException("Customer with Id : " + username + " does not exist");
        }
        UserEntity customer = customerOptional.get();

        log.info("customer id : {}", customer.getUserId());

        List<ProductCart> productCart = productCartRepository.findProductCartByCustomer(customer);


        log.info("productCart : {}", productCart);

        for(ProductCart product: productCart){
            productCartRepository.delete(product);
        }

        return ResponseUtils.createDefaultSuccessResponse();
    }
}
