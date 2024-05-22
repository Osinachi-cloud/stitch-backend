package com.stitch.service.serviceImpl;

import com.stitch.commons.exception.StitchException;
import com.stitch.commons.model.dto.PaginatedResponse;
import com.stitch.commons.model.dto.Response;
import com.stitch.commons.util.ResponseUtils;
import com.stitch.model.dto.ProductDto;
import com.stitch.model.entity.Product;
import com.stitch.model.entity.ProductLike;
import com.stitch.repository.ProductLikeRepository;
import com.stitch.repository.ProductRepository;
import com.stitch.service.ProductLikeService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductLikeServiceImpl implements ProductLikeService {

    private final ProductLikeRepository productLikeRepository;
    private final ProductRepository productRepository;

    private final CustomerRepository customerRepository;

    public ProductLikeServiceImpl(ProductLikeRepository productLikeRepository, ProductRepository productRepository, CustomerRepository customerRepository) {
        this.productLikeRepository = productLikeRepository;
        this.productRepository = productRepository;

        this.customerRepository = customerRepository;
    }

    @Override
    public Response addToLikes(String productId){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<Customer> customerOptional = customerRepository.findByEmailAddress(username);
        if(customerOptional.isEmpty()){
            throw new StitchException("Customer with Id : " + username + " does not exist");
        }

        Customer customer = customerOptional.get();

        Optional<ProductLike> existingProductLike = productLikeRepository.findByProductId(productId);
        if(existingProductLike.isPresent()){
            throw new StitchException("Product with Id : " + productId + " already exist");
        }

        Optional<Product> existingProduct = productRepository.findByProductId(productId);
        if(existingProduct.isEmpty()){
            throw new StitchException("Product with Id : " + productId + " does not exist");
        }

        ProductLike productLike = new ProductLike();
        productLike.setProductId(productId);
        productLike.setCustomer(customer);

        productLikeRepository.save(productLike);
        return ResponseUtils.createDefaultSuccessResponse();
    }

    @Override
    public Response removeFromLikes(String productId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<ProductLike> existingProductLike = productLikeRepository.findByProductId(productId);
        if(existingProductLike.isEmpty()){
            throw new StitchException("Product Liked with Id : " + productId + " does not exist");
        }
        productLikeRepository.delete(existingProductLike.get());
        return ResponseUtils.createDefaultSuccessResponse();
    }


    @Override
    public PaginatedResponse<List<ProductDto>> getAllLikes(int page, int size){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<Customer> customerOptional = customerRepository.findByEmailAddress(username);
        if(customerOptional.isEmpty()){
            throw new StitchException("Customer with Id : " + username + " does not exist");
        }

        Customer customer = customerOptional.get();

        Pageable pagerequest = PageRequest.of(page, size);

         Page<ProductLike> productLikes = productLikeRepository.findProductLikesByCustomer(customer, pagerequest);

         log.info("productLikes : {} " + customer.getCustomerId() + " ", productLikes.getContent());

        PaginatedResponse<List<ProductDto>> paginatedResponse = new PaginatedResponse<>();
        paginatedResponse.setPage(productLikes.getNumber());
        paginatedResponse.setSize(productLikes.getSize());
        paginatedResponse.setTotal((int) productLikeRepository.getLikeCount(customer.getCustomerId()));
        paginatedResponse.setData(convertProductLikeListToDto(productLikes.getContent()));
        return paginatedResponse;
    }


    private List<ProductDto> convertProductLikeListToDto(List<ProductLike> productLikeList){

        List<ProductDto> productDtoList = new ArrayList<>();
        for(ProductLike productLike: productLikeList){
            Optional<Product> productExists = productRepository.findByProductId(productLike.getProductId());
            if(productExists.isEmpty()){
                throw new StitchException("Product with : " + productLike.getProductId()  + " does not exist");
            }
            Product product = productExists.get();

            ProductDto productDto = new ProductDto();
            BeanUtils.copyProperties(product, productDto);
            productDtoList.add(productDto);
        }
        return productDtoList;
    }
}
