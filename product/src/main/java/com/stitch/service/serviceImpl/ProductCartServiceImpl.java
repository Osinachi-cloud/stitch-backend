package com.stitch.service.serviceImpl;

import com.stitch.commons.exception.StitchException;
import com.stitch.commons.model.dto.PaginatedResponse;
import com.stitch.commons.model.dto.Response;
import com.stitch.exception.CartException;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import static com.stitch.commons.util.Constants.getStr;
import static com.stitch.commons.util.ResponseUtils.createDefaultSuccessResponse;
import static com.stitch.commons.util.SharedUtils.getLoggedInUser;


@Service
@Slf4j
public class ProductCartServiceImpl implements ProductCartService {

    private final ProductCartRepository productCartRepository;
    private final ProductRepository productRepository;

    private final UserRepository customerRepository;

    public ProductCartServiceImpl(ProductCartRepository productCartRepository, ProductRepository productRepository, UserRepository customerRepository) {
        this.productCartRepository = productCartRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    private void validateId(String id, String type) {
        if (Objects.isNull(id) || id.trim().isEmpty()) {
            throw new CartException(String.format("%s is required", type), 400);
        }
    }

    @Override
    public Response addToCart(String productId) {
        log.info("productId to add to cart: {}", productId);
        try {
            Map<String, Object> details = validateProductWithCustomer(productId);
            Product product = safeCast(details, "product", Product.class);
            UserEntity customer = safeCast(details, "customer", UserEntity.class);
            log.info("existing Product fetched : {} \n Existing user : {} ", product, customer);
            Optional<ProductCart> existingProductCart = productCartRepository.findByProductId(productId);
            return updateCartDetails(existingProductCart, product, customer, productId, Optional.empty());

        } catch (CartException e) {
            log.error("Custom error occurred while adding to cart for product id : {} : {}", productId, e.getMessage());
            throw new CartException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            log.error("An exception occurred while adding product to card for id : {} : {}", productId, e.getMessage());
            throw new CartException(e.getMessage(), 400);
        }

    }

    private static <T> T safeCast(Map<String, Object> map, String key, Class<T> type) {
        Object value = map.get(key);
        if (type.isInstance(value)) {
            return type.cast(value);
        }
        throw new ClassCastException("Expected " + type.getSimpleName() + " for key '" + key + "', but got " + (value == null ? "null" : value.getClass().getSimpleName()));
    }

    @Override
    public Response addToCart(String productId, ProductVariationRequest productVariationDto) {
        log.info("productId cart: {}", productId);
        log.info("productVariationDto: {}", productVariationDto);

        try {
            Map<String, Object> details = validateProductWithCustomer(productId, productVariationDto);
            Product product = safeCast(details, "product", Product.class);
            UserEntity customer = safeCast(details, "customer", UserEntity.class);
            log.info("existing Product fetched : {} \n Existing user : {} ", product, customer);
            Optional<ProductCart> existingProductCart = productCartRepository.findByProductIdAndColorAndSleeveTypeAndMeasurementTag(productId, productVariationDto.getColor(),
                    productVariationDto.getSleeveType(), productVariationDto.getMeasurementTag());
            return updateCartDetails(existingProductCart, product, customer, productId, Optional.of(productVariationDto));

        } catch (CartException e) {
            log.error("Custom error occurred while adding to cart with variation for product id : {} : {}", productId, e.getMessage());
            throw new CartException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            log.error("An exception occurred while adding product to cart with variation for id : {} : {}", productId, e.getMessage());
            throw new CartException(e.getMessage(), 400);
        }

    }

    @Override
    public Response increaseToCart(String productId, int quantity, ProductVariationRequest productVariationDto) {
        log.info("productId cart: {}", productId);
        log.info("productVariationDto: {}", productVariationDto);

        try {
            Map<String, Object> details = validateProductWithCustomer(productId, productVariationDto);
            Product product = safeCast(details, "product", Product.class);
            UserEntity customer = safeCast(details, "customer", UserEntity.class);
            log.info("existing Product fetched : {} \n Existing user : {} ", product, customer);
            Optional<ProductCart> existingProductCart = productCartRepository.findByProductIdAndColorAndSleeveTypeAndMeasurementTag(productId, productVariationDto.getColor(),
                    productVariationDto.getSleeveType(), productVariationDto.getMeasurementTag());
            return increaseCartDetails(existingProductCart, product, customer, productId, Optional.of(productVariationDto), quantity);

        } catch (CartException e) {
            log.error("Custom error occurred while adding to cart with variation for product id : {} : {}", productId, e.getMessage());
            throw new CartException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            log.error("An exception occurred while adding product to cart with variation for id : {} : {}", productId, e.getMessage());
            throw new CartException(e.getMessage(), 400);
        }

    }

    private void setVariationDetails(ProductCart productCart, ProductVariationRequest productVariationDto) {
        productCart.setColor(getStr(productVariationDto.getColor()));
        productCart.setSleeveType(getStr(productVariationDto.getSleeveType()));
        productCart.setMeasurementTag(getStr(productVariationDto.getMeasurementTag()));
    }

    private Response updateCartDetails(Optional<ProductCart> existingProductCart, Product product, UserEntity customer, String productId, Optional<ProductVariationRequest> productVariation) {
        if (existingProductCart.isPresent()) {
            log.info("existing Product Cart is present : {}", existingProductCart.get());
            ProductCart productCart = existingProductCart.get();
            productCart.setQuantity(productCart.getQuantity() + 1);
            productCart.setProductCategoryName(product.getCategory().name());
            System.out.println("vendor :" + product.getVendor());
            productCart.setVendor(product.getVendor());
            productCart.setAmountByQuantity(product.getPrice().multiply(BigDecimal.valueOf(productCart.getQuantity())));
            productCartRepository.save(productCart);

        } else {
            log.info("existing Product Cart is not present");
            ProductCart productCart = new ProductCart();
            productCart.setProductId(productId);
            productCart.setCustomer(customer);
            productCart.setVendor(product.getVendor());
            productCart.setQuantity(1);
            productCart.setAmountByQuantity(product.getPrice().multiply(BigDecimal.valueOf(productCart.getQuantity())));
            productVariation.ifPresent(productVariationRequest -> setVariationDetails(productCart, productVariationRequest));
            productCartRepository.save(productCart);
        }
        return createDefaultSuccessResponse();

    }

    private Response increaseCartDetails(Optional<ProductCart> existingProductCart, Product product, UserEntity customer, String productId, Optional<ProductVariationRequest> productVariation, int quantity) {
        if (existingProductCart.isPresent()) {
            log.info("existing Product Cart is present : {}", existingProductCart.get());
            ProductCart productCart = existingProductCart.get();
            productCart.setQuantity(productCart.getQuantity() + 1);
            productCart.setProductCategoryName(product.getCategory().name());
            System.out.println("vendor :" + product.getVendor());
            productCart.setVendor(product.getVendor());
            productCart.setAmountByQuantity(product.getPrice().multiply(BigDecimal.valueOf(productCart.getQuantity())));
            productCartRepository.save(productCart);

        } else {
            log.info("existing Product Cart is not present");
            ProductCart productCart = new ProductCart();
            productCart.setProductId(productId);
            productCart.setCustomer(customer);
            productCart.setVendor(product.getVendor());
            productCart.setQuantity(quantity);
            productCart.setAmountByQuantity(product.getPrice().multiply(BigDecimal.valueOf(productCart.getQuantity())));
            productVariation.ifPresent(productVariationRequest -> setVariationDetails(productCart, productVariationRequest));
            productCartRepository.save(productCart);
        }
        return createDefaultSuccessResponse();

    }
    private Map<String, Object> validateProductWithCustomer(String productId, ProductVariationRequest productVariationDto) {
        if(Objects.isNull(productVariationDto.getMeasurementTag()) || productVariationDto.getMeasurementTag().trim().isEmpty()) {
            throw new CartException("Body measurement is required", 400);
        }

        if(Objects.isNull(productVariationDto.getSleeveType()) || productVariationDto.getSleeveType().trim().isEmpty()) {
            throw new CartException("Sleeve Type is required", 400);
        }

        if(Objects.isNull(productVariationDto.getColor()) || productVariationDto.getColor().trim().isEmpty()) {
            throw new CartException("Color is required", 400);
        }

        validateId(productId, "product Id");
        String username = getLoggedInUser()
                .orElseThrow(() -> new CartException("Failed to authenticate user", 403));

        UserEntity customer = customerRepository.findByEmailAddress(username)
                .orElseThrow(() -> new CartException("Customer with Id : " + username + " does not exist", 404));

        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new CartException("Product with Id : " + productId + " does not exist", 404));

        return Map.of("customer", customer, "product", product);
    }

    private Map<String, Object> validateProductWithCustomer(String productId) {
        validateId(productId, "product Id");
        String username = getLoggedInUser()
                .orElseThrow(() -> new CartException("Failed to authenticate user", 403));

        UserEntity customer = customerRepository.findByEmailAddress(username)
                .orElseThrow(() -> new CartException("Customer with Id : " + username + " does not exist", 404));

        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new CartException("Product with Id : " + productId + " does not exist", 404));

        return Map.of("customer", customer, "product", product);
    }

    @Override
    public Response removeOrReduceFromCart(String productId, ProductVariationRequest productVariationDto) {

        try {
            validateId(productId, "product Id");
            String color = getStr(productVariationDto.getColor());
            String sleeveType = getStr(productVariationDto.getSleeveType());
            String measurementTag = getStr(productVariationDto.getMeasurementTag());
            log.info("cart update with parameters :  color : {}, sleeve type : {}, tag : {}, product Id {}",
                    color, sleeveType, measurementTag, productId);
            Product product = productRepository.findByProductId(productId)
                    .orElseThrow(() -> new CartException("Product with Id : " + productId + " does not exist", 404));

            ProductCart productCart = productCartRepository.findByProductIdAndColorAndSleeveTypeAndMeasurementTag(productId, color, sleeveType, measurementTag)
                    .orElseThrow(() -> new CartException("Product in cart with Id : " + productId + " does not exist", 404));

            if (productCart.getQuantity() > 1) {
                productCart.setQuantity(productCart.getQuantity() - 1);
                productCart.setAmountByQuantity(product.getPrice().multiply(BigDecimal.valueOf(productCart.getQuantity())));
                productCartRepository.save(productCart);
            } else {
                productCartRepository.delete(productCart);
            }
            return createDefaultSuccessResponse();
        } catch (CartException e) {
            log.error("Custom error while updating cart for product id : {} : {}", productId, e.getMessage());
            throw new CartException(e.getMessage(), e.getCode());

        } catch (Exception e) {
            log.error("An exception occurred in cart update for product id {}  : {}", productId, e.getMessage());
            throw new CartException(e.getMessage(), 400);

        }

    }

    @Override
    public Response removeProductFromCart(String productId, ProductVariationRequest productVariationDto) {
        try {
            String color = getStr(productVariationDto.getColor());
            String sleeveType = getStr(productVariationDto.getSleeveType());
            String tag = getStr(productVariationDto.getMeasurementTag());
            log.info("cart delete with parameters :  color : {}, sleeve type : {}, tag : {}, product Id {}",
                    color, sleeveType, tag, productId);
            ProductCart productCart = productCartRepository.findByProductIdAndColorAndSleeveTypeAndMeasurementTag(productId, color, sleeveType, tag)
                    .orElseThrow(() -> new CartException("Product in cart with Id : " + productId + " does not exist", 404));
            productCartRepository.delete(productCart);
            return createDefaultSuccessResponse();
        } catch (Exception e) {
            log.error("Error occurred removing product with id {} form cart :: {}", productId, e.getMessage());
            throw new CartException(e.getMessage(), 400);
        }

    }

    @Override

    public PaginatedResponse<List<CartDto>> getCart(int page, int size) {

        try {
            String username = getLoggedInUser()
                    .orElseThrow(() -> new CartException("Failed to authenticate user", 403));
            UserEntity customer = customerRepository.findByEmailAddress(username)
                    .orElseThrow(() -> new CartException("Customer with Id : " + username + " does not exist", 404));
            Pageable pageRequest = PageRequest.of(page, size);

            log.info("customer id to get cart: {}, page :{}, size:{}", customer.getEmailAddress(), page,size);
            Page<ProductCart> productCart = productCartRepository.findProductCartByCustomer(customer, pageRequest);

            log.info("productCart content: {}", productCart.getContent());
            PaginatedResponse<List<CartDto>> paginatedResponse = new PaginatedResponse<>();
            paginatedResponse.setPage(productCart.getNumber());
            paginatedResponse.setSize(productCart.getSize());
            paginatedResponse.setTotal(productCartRepository.getCartCount(customer.getEmailAddress()));
            paginatedResponse.setData(convertProductCartListToDto(productCart.getContent()));
            return paginatedResponse;
        } catch (CartException e) {
            log.error("Custom error getting customer cart :: {}", e.getMessage());
            throw new CartException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            log.error("Exception getting customer cart :: {}", e.getMessage());
            throw new CartException(e.getMessage(), 400);

        }
    }


    private List<CartDto> convertProductCartListToDto(List<ProductCart> productCartList) {

        List<CartDto> productDtoList = new ArrayList<>();
        for (ProductCart productCart : productCartList) {
            Optional<Product> productExists = productRepository.findByProductId(productCart.getProductId());
            if (productExists.isEmpty()) {
                throw new StitchException("Product with : " + productCart.getProductId() + " does not exist");
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
    public Map<String, BigDecimal> sumAmountByQuantityByCustomerId() {
        try {
            String username = getLoggedInUser()
                    .orElseThrow(() -> new CartException("Failed to authenticate user", 403));
            UserEntity customer = customerRepository.findByEmailAddress(username)
                    .orElseThrow(() -> new CartException("Customer with Id : " + username + " does not exist", 404));
            BigDecimal sum = productCartRepository.sumAmountByQuantityByUserId(customer.getEmailAddress());
            return Map.of("sum", sum);
        } catch (CartException e) {
            log.error("Custom error getting sum :: {}", e.getMessage());
            throw new CartException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            log.error("Exception getting sum :: {}", e.getMessage());
            throw new CartException(e.getMessage(), 400);
        }

    }

    @Override
    @Transactional
    public Response clearCart() {
        try {
            String username = getLoggedInUser()
                    .orElseThrow(() -> new CartException("User not logged in", 403));

            UserEntity customer = customerRepository.findByEmailAddress(username)
                    .orElseThrow(() -> new CartException("Customer with username : " + username + " does not exist", 404));
            log.info("customer id : {}", customer.getUserId());

            List<ProductCart> productCart = productCartRepository.findProductCartByCustomer(customer);
            log.info("product Cart : {}", productCart);
            productCartRepository.deleteAll(productCart);
            return createDefaultSuccessResponse();

        } catch (CartException e) {
            log.error("Custom Error occurred while clearing cart : {}", e.getMessage());
            throw new CartException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            log.error("Error occurred while clearing cart : {}", e.getMessage());
            throw new CartException(e.getMessage(), 400);
        }
    }

    @Transactional
    public Response moveCartToOrder() {
        try {
            String username = getLoggedInUser()
                    .orElseThrow(() -> new CartException("Failed to authenticate user", 403));
            UserEntity customer = customerRepository.findByEmailAddress(username)
                    .orElseThrow(() -> new CartException("Customer with username : " + username + " does not exist", 404));
            log.info("customer id for move to cart : {}", customer.getUserId());

            List<ProductCart> productCart = productCartRepository.findProductCartByCustomer(customer);
            log.info("productCart : {}", productCart);
            productCartRepository.deleteAll(productCart);
            return createDefaultSuccessResponse();
        } catch (CartException e) {
            log.error("Custom error moving cart to order :: {}", e.getMessage());
            throw new CartException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            log.error("Error occurred while moving cart to order :: {}", e.getMessage());
            throw new CartException(e.getMessage(), 400);
        }
    }
}
