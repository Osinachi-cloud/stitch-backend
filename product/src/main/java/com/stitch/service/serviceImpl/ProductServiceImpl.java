package com.stitch.service.serviceImpl;

import com.stitch.ProductSpecification;
import com.stitch.commons.enums.ResponseStatus;
import com.stitch.commons.exception.StitchException;
import com.stitch.commons.model.dto.PaginatedResponse;
import com.stitch.commons.model.dto.Response;
import com.stitch.commons.util.NumberUtils;
import com.stitch.commons.util.ResponseUtils;
import com.stitch.model.ProductCategory;
import com.stitch.model.dto.ProductDto;
import com.stitch.model.dto.ProductFilterRequest;
import com.stitch.model.dto.ProductRequest;
import com.stitch.model.dto.ProductUpdateRequest;
import com.stitch.model.entity.Product;
import com.stitch.model.enums.PublishStatus;
import com.stitch.repository.ProductRepository;
import com.stitch.service.ProductService;
import com.stitch.user.enums.Tier;
import com.stitch.user.exception.UserException;
import com.stitch.user.model.dto.VendorDto;
import com.stitch.user.model.entity.Device;
import com.stitch.user.model.entity.Vendor;
import com.stitch.user.repository.VendorRepository;
import jakarta.servlet.ServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.stitch.utils.ProductUtils.convertProductListToDto;
import static com.stitch.utils.ProductUtils.convertProductToDto;
import static java.lang.Math.floorDiv;
import static java.lang.Math.toIntExact;

@Service
public class ProductServiceImpl implements ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    private final VendorRepository vendorRepository;

    public ProductServiceImpl(ProductRepository productRepository, VendorRepository vendorRepository) {
        this.productRepository = productRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public ProductDto createProduct(ProductRequest productRequest) {

        log.info("products one : {}", productRequest);

        validateProductRequest(productRequest);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Optional<Vendor> vendorExist = vendorRepository.findByEmailAddress(email);
        if (vendorExist.isEmpty()){
            throw new UserException("Vendor with : " + email + " does not exist");
        }

        Vendor vendor = vendorExist.get();

        Product product = new Product();
        product.setProductId(NumberUtils.generate(10));
        product.setProductImage(productRequest.getProductImage());
        product.setCategory(ProductCategory.valueOf(productRequest.getCategory()));
        product.setFixedPrice(productRequest.isFixedPrice());
        product.setQuantity(productRequest.getQuantity());
        product.setAmount(productRequest.getAmount());
        product.setName(productRequest.getName());
        product.setCode(productRequest.getCode());
        product.setShortDescription(productRequest.getShortDescription());
        product.setLongDescription(productRequest.getLongDescription());
        product.setSellingPrice(productRequest.getAmount().subtract( productRequest.getAmount().multiply(productRequest.getDiscount()).divide(BigDecimal.valueOf(100))));
        product.setMaterialUsed(productRequest.getMaterialUsed());
        product.setReadyIn(productRequest.getReadyIn());
        product.setDiscount(productRequest.getDiscount());
        product.setVendor(vendor);
        product.setPublishStatus(PublishStatus.valueOf(productRequest.getPublishStatus()));

        Product savedProduct = productRepository.save(product);

        log.info("saved Product : {}", savedProduct);

        return convertProductToDto(savedProduct);
    }

    public  void validateProductRequest(ProductRequest productRequest){

        if(!Objects.nonNull(productRequest)){
           throw new UserException("product request can not be null");
        }
        if(StringUtils.isBlank(productRequest.getProductImage())
        || StringUtils.isBlank(productRequest.getName())
        || StringUtils.isBlank(productRequest.getCode())
        || StringUtils.isBlank(productRequest.getCategory())){
            throw new UserException(ResponseStatus.EMPTY_FIELD_VALUES);
        }

//        for(ProductCategory val : ProductCategory.values()){
//            if(!val.toString().equals(productRequest.getCategory())){
//                System.out.println("=====================");
//                System.out.println(productRequest.getCategory());
//                System.out.println("=====================");
//
//                throw new UserException("Invalid category type");
//
//            }
//        }
    }

    @Override
    public ProductDto updateProduct(ProductUpdateRequest productRequest, String productId) {

        Product product = productRepository.findByProductId(productId).orElseThrow(()-> new StitchException("Product with id: " + productId + "  can not be found"));

        product.setProductImage(productRequest.getProductImage());
        product.setCategory(ProductCategory.valueOf(productRequest.getCategory()));
        product.setFixedPrice(productRequest.isFixedPrice());
        product.setQuantity(productRequest.getQuantity());
        product.setAmount(productRequest.getAmount());
        product.setName(productRequest.getName());
        product.setCode(productRequest.getCode());

        Product updatedProduct = productRepository.save(product);

        return convertProductToDto(updatedProduct);

    }

    @Override
    public Response updateProductProfileImage(String profileImage, String productId) {

        try{
            Product product = productRepository.findByProductId(productId).orElseThrow(()-> new StitchException("Product with id: " + productId + "  can not be found"));
            product.setProductImage(profileImage);
            productRepository.save(product);
            return ResponseUtils.createSuccessResponse("product image updated successfully");

        }catch (Exception e){
            throw new StitchException("error in updating product image");
        }
    }

    @Override
    public ProductDto getProductByProductId(String productId) {

        Product product = productRepository.findByProductId(productId).orElseThrow(
                ()-> new StitchException("Product with id: " + productId + "  can not be found"));
        return convertProductToDto(product);
    }

    @Override
    public PaginatedResponse<List<ProductDto>> getProductByVendor(String vendorId, Pageable pageable) {

        log.info( " vendor ID : {}",vendorId);

        Page<Product> productPage = productRepository.findProductsByVendorId(vendorId, pageable);
        List<Product> productList = productPage.getContent();

        log.info( " productList : {}",vendorId);


        PaginatedResponse<List<ProductDto>> paginatedResponse = new PaginatedResponse<>();

        paginatedResponse.setSize(productPage.getSize());
        paginatedResponse.setData(convertProductListToDto(productList));
        paginatedResponse.setTotal(toIntExact(productPage.getTotalElements()));
        paginatedResponse.setPage(productPage.getNumber());

        return paginatedResponse;

    }

    @Override
    public void deleteProduct(String productId) {
        productRepository.deleteByProductId(productId);
    }


//    public class DelayedPrinter {
//        public static void main(String[] args) {
//            printWordsWithDelay("Hello", "World");
//        }
//
//        public static void printWordsWithDelay(String word1, String word2) {
//            Thread thread1 = new Thread(() -> {
//                try {
//                    Thread.sleep(2000); // Delay for 2 seconds
//                    System.out.println(word1);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            });
//
//            Thread thread2 = new Thread(() -> {
//                try {
//                    Thread.sleep(4000); // Delay for 4 seconds
//                    System.out.println(word2);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            });
//
//            thread1.start();
//            thread2.start();
//        }
//    }




    @Override
    public PaginatedResponse<List<ProductDto>> fetchAllProducts(ProductFilterRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailAddress = authentication.getName();
        Optional<Vendor> vendorExists = vendorRepository.findByEmailAddress(emailAddress);

        if(vendorExists.isEmpty()){
            throw new UserException("Vendor with Id: " + request.getVendorId() + " does not exist");
        }

        Specification<Product> spec = Specification.where(
                        ProductSpecification.nameEqual(request.getName()))
                .and(ProductSpecification.categoryEqual(request.getCategory()))
                .and(ProductSpecification.codeEqual(request.getCode()))
                .and(ProductSpecification.productIdEqual(request.getProductId()))
                .and(ProductSpecification.vendorEqual(vendorExists.get()));

        Page<Product> products = productRepository.findAll(spec, PageRequest.of(request.getPage(), request.getSize(), Sort.by(Sort.Direction.DESC, "dateCreated")));

        log.info("products all : {}", products.getContent());
        PaginatedResponse<List<ProductDto>> paginatedResponse = new PaginatedResponse<>();
        paginatedResponse.setPage(products.getNumber());
        paginatedResponse.setSize(products.getSize());
        paginatedResponse.setTotal((int) productRepository.count());
        paginatedResponse.setData(convertProductListToDto(products.getContent()));
        return paginatedResponse;
    }

    @Override
    public boolean togglePublishProduct(String productId) {

        System.out.println(productId);

        boolean publishedStatus = false;

        Optional <Product> existingProduct = productRepository.findByProductId(productId);
        if(existingProduct.isEmpty()){
            throw new UserException("product with id : " + productId + " is not found");
        }
        Product product = existingProduct.get();

        if(product.getPublishStatus().equals(PublishStatus.PUBLISH)){
            product.setPublishStatus(PublishStatus.UNPUBLISH);
            productRepository.save(product);
        }else {
            product.setPublishStatus(PublishStatus.PUBLISH);
            productRepository.save(product);
            publishedStatus = true;
        }
        log.info("product info : {}", product);
        return publishedStatus;
    }


}
