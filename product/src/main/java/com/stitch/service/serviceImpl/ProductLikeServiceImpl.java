package com.stitch.service.serviceImpl;

import com.stitch.commons.exception.StitchException;
import com.stitch.commons.model.dto.PaginatedResponse;
import com.stitch.commons.model.dto.Response;
import com.stitch.commons.util.ResponseUtils;
import com.stitch.exception.ProductException;
import com.stitch.model.dto.ProductDto;
import com.stitch.model.entity.Product;
import com.stitch.model.entity.ProductLike;
import com.stitch.repository.ProductLikeRepository;
import com.stitch.repository.ProductRepository;
import com.stitch.service.ProductLikeService;
import com.stitch.user.model.entity.UserEntity;
import com.stitch.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.stitch.commons.util.SharedUtils.getLoggedInUser;

@Service
@Slf4j
public class ProductLikeServiceImpl implements ProductLikeService {

    private final ProductLikeRepository productLikeRepository;
    private final ProductRepository productRepository;

    private final UserRepository customerRepository;

    public ProductLikeServiceImpl(ProductLikeRepository productLikeRepository, ProductRepository productRepository, UserRepository customerRepository) {
        this.productLikeRepository = productLikeRepository;
        this.productRepository = productRepository;

        this.customerRepository = customerRepository;
    }

    @Override
    public Response addToLikes(String productId) {
        try {
            String username = getLoggedInUser()
                    .orElseThrow(() -> new ProductException("Failed to authenticate user", 403));
            UserEntity customer = customerRepository.findByEmailAddress(username)
                    .orElseThrow(() -> new ProductException("Customer with username : " + username + " does not exist", 404));

            Optional<ProductLike> existingProductLike = productLikeRepository.findByProductIdAndUserEntity(productId, customer);
            if (existingProductLike.isPresent()) {
                productLikeRepository.delete(existingProductLike.get());
                return ResponseUtils.createResponse(204, "has been removed from like list");
            }

            productRepository.findByProductId(productId)
                    .orElseThrow(() -> new ProductException("Product with id : " + productId + " does not exist", 404));

            ProductLike productLike = new ProductLike();
            productLike.setProductId(productId);
            productLike.setUserEntity(customer);
            productLikeRepository.save(productLike);
            return ResponseUtils.createResponse(204, "Successfully added to Likes");

        } catch (ProductException e) {
            log.error("Custom error adding likes :: {}", e.getMessage());
            throw new ProductException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            log.error("Exception occurred adding likes :: {}", e.getMessage());
            throw new ProductException(e.getMessage(), 400);
        }
    }

    @Override
    public Response removeFromLikes(String productId) {
        try {
            String username = getLoggedInUser()
                    .orElseThrow(() -> new ProductException("Failed to authenticate user", 403));
            UserEntity customer = customerRepository.findByEmailAddress(username)
                    .orElseThrow(() -> new ProductException("Customer with username : " + username + " does not exist", 404));
            ProductLike productLike = productLikeRepository.findByProductIdAndUserEntity(productId, customer)
                    .orElseThrow(() -> new ProductException("Product Liked with Id : " + productId + " does not exist", 404));
            productLikeRepository.delete(productLike);
            return ResponseUtils.createDefaultSuccessResponse();
        } catch (Exception e) {
            log.error("Exception occurred removing likes :: {}", e.getMessage());
            throw new ProductException(e.getMessage(), 400);
        }
    }

    @Override
    public PaginatedResponse<List<ProductDto>> getAllLikes(int page, int size) {

        try {
            String username = getLoggedInUser()
                    .orElseThrow(() -> new ProductException("Failed to authenticate user", 403));
            UserEntity customer = customerRepository.findByEmailAddress(username)
                    .orElseThrow(() -> new ProductException("Customer with username : " + username + " does not exist", 404));

            Pageable pageRequest = PageRequest.of(page, size);

            Page<ProductLike> productLikes = productLikeRepository.findProductLikesByUserEntity(customer, pageRequest);
            PaginatedResponse<List<ProductDto>> paginatedResponse = new PaginatedResponse<>();
            paginatedResponse.setPage(productLikes.getNumber());
            paginatedResponse.setSize(productLikes.getSize());
            paginatedResponse.setTotal(productLikeRepository.getLikeCount(username));
            paginatedResponse.setData(convertProductLikeListToDto(productLikes.getContent()));
            return paginatedResponse;
        } catch (Exception e) {
            log.error("Exception occurred getting likes :: {}", e.getMessage());
            throw new ProductException(e.getMessage(), 400);
        }

    }

    private List<ProductDto> convertProductLikeListToDto(List<ProductLike> productLikeList) {

        List<ProductDto> productDtoList = new ArrayList<>();
        for (ProductLike productLike : productLikeList) {
            Optional<Product> productExists = productRepository.findByProductId(productLike.getProductId());
            if (productExists.isEmpty()) {
                throw new ProductException("Product with : " + productLike.getProductId() + " does not exist",404);
            }
            Product product = productExists.get();

            ProductDto productDto = new ProductDto();
            BeanUtils.copyProperties(product, productDto);
            productDtoList.add(productDto);
        }
        return productDtoList;
    }
}
