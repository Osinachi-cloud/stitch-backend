package com.stitch.service.serviceImpl;

import com.stitch.ProductSpecification;
import com.stitch.commons.enums.ResponseStatus;
import com.stitch.commons.exception.StitchException;
import com.stitch.commons.model.dto.PaginatedResponse;
import com.stitch.commons.model.dto.Response;
import com.stitch.commons.util.NumberUtils;
import com.stitch.commons.util.ResponseUtils;
import com.stitch.model.ProductCategory;
import com.stitch.model.dto.*;
import com.stitch.model.entity.Product;
import com.stitch.model.entity.ProductLike;
import com.stitch.model.entity.ProductVariation;
import com.stitch.model.enums.PublishStatus;
import com.stitch.repository.ProductLikeRepository;
import com.stitch.repository.ProductRepository;
import com.stitch.repository.ProductVariationRepository;
import com.stitch.service.ProductService;
import com.stitch.user.exception.UserException;
import com.stitch.user.model.dto.UserDto;
import com.stitch.user.model.entity.UserEntity;
import com.stitch.user.repository.UserRepository;
import com.stitch.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.stitch.utils.ProductUtils.convertProductListToDto;
import static com.stitch.utils.ProductUtils.convertProductVariationDtoListToEntity;
import static java.lang.Math.toIntExact;

@Service
public class ProductServiceImpl implements ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    private final ProductLikeRepository productLikeRepository;

    private final UserRepository userRepository;

    private final UserService userService;

    private final ProductVariationRepository productVariationRepository;

    public ProductServiceImpl(ProductRepository productRepository, ProductLikeRepository productLikeRepository, UserRepository userRepository, UserService userService, ProductVariationRepository productVariationRepository) {
        this.productRepository = productRepository;
        this.productLikeRepository = productLikeRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.productVariationRepository = productVariationRepository;
    }

    @Override
    @PreAuthorize("hasAuthority('VENDOR')")
    public ProductDto createProduct(ProductRequest productRequest) {

        log.info("products one : {}", productRequest);

        validateProductRequest(productRequest);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Optional<UserEntity> customerExist = userRepository.findByEmailAddress(email);
        if (customerExist.isEmpty()){
            throw new UserException("Vendor with : " + email + " does not exist");
        }

        UserEntity customer = customerExist.get();

        Product product = new Product();
        String prodId = NumberUtils.generate(10);
        product.setProductId(prodId);
        product.setProductImage(productRequest.getProductImage());
        product.setCategory(ProductCategory.valueOf(productRequest.getCategory()));
        product.setFixedPrice(productRequest.isFixedPrice());
        product.setQuantity(productRequest.getQuantity());
        product.setPrice(productRequest.getPrice());
        product.setName(productRequest.getName());
        product.setCode(productRequest.getCode());
        product.setShortDescription(productRequest.getShortDescription());
        product.setLongDescription(productRequest.getLongDescription());
        product.setSellingPrice(productRequest.getPrice().subtract( productRequest.getPrice().multiply(productRequest.getDiscount()).divide(BigDecimal.valueOf(100))));
        product.setMaterialUsed(productRequest.getMaterialUsed());
        product.setReadyIn(productRequest.getReadyIn());
        product.setDiscount(productRequest.getDiscount());
        product.setVendor(customer);

        log.info("productRequest.getProductVariationDtoList() : {}" , productRequest.getProductVariation());

        List<ProductVariation> productVariationList = convertProductVariationDtoListToEntity(productRequest.getProductVariation());
        List<ProductVariation> savedProductVariationList = productVariationRepository.saveAll(productVariationList);

        product.setProductVariation(savedProductVariationList);

        product.setPublishStatus(PublishStatus.valueOf(productRequest.getPublishStatus()));

        if(Objects.nonNull(productRequest.getProductImage())){
            byte[] imageBytes = Base64.decodeBase64(productRequest.getProductImage());
            String base64EncodedImage = Base64.encodeBase64String(imageBytes);
            product.setProductImage(base64EncodedImage);
        }

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

    }

    @Override
    @PreAuthorize("hasAuthority('VENDOR')")
    public ProductDto updateProduct(ProductUpdateRequest productRequest, String productId) {

        Product product = productRepository.findByProductId(productId).orElseThrow(()-> new StitchException("Product with id: " + productId + "  can not be found"));

        product.setProductImage(productRequest.getProductImage());
        product.setCategory(ProductCategory.valueOf(productRequest.getCategory()));
        product.setFixedPrice(productRequest.isFixedPrice());
        product.setQuantity(productRequest.getQuantity());
        product.setPrice(productRequest.getAmount());
        product.setName(productRequest.getName());
        product.setCode(productRequest.getCode());

        Product updatedProduct = productRepository.save(product);

        return convertProductToDto(updatedProduct);

    }

    @Override
    @PreAuthorize("hasAuthority('VENDOR')")
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
//    @PreAuthorize("hasAuthority('VENDOR')")
    public ProductDto getProductByProductId(String productId) {

        Product product = productRepository.findByProductId(productId).orElseThrow(
                ()-> new StitchException("Product with id: " + productId + "  can not be found"));
        return convertProductToDto(product);
    }

    @Override
    @PreAuthorize("hasAuthority('VENDOR')")
    public PaginatedResponse<List<ProductDto>> getProductByVendor(String vendorId, Pageable pageable) {

        log.info( " vendor ID : {}",vendorId);

        UserEntity customer = userService.getCustomerEntity(vendorId);

        Page<Product> productPage = productRepository.findProductsByVendor(customer, pageable);
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
    @PreAuthorize("hasAuthority('VENDOR')")
    public void deleteProduct(String productId) {
        productRepository.deleteByProductId(productId);
    }

    @Override
    @PreAuthorize("hasAuthority('VENDOR')")
    public PaginatedResponse<List<ProductDto>> fetchAllProductsByVendor(ProductFilterRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailAddress = authentication.getName();
        Optional<UserEntity> vendorExists = userRepository.findByEmailAddress(emailAddress);

        if(vendorExists.isEmpty()){
            throw new UserException("Vendor with Id: " + request.getVendorId() + " does not exist");
        }

        Specification<Product> spec = Specification.where(
                        ProductSpecification.nameEqual(request.getName()))
                .and(ProductSpecification.categoryIn(request.getCategories()))
                .and(ProductSpecification.codeEqual(request.getCode()))
                .and(ProductSpecification.productIdEqual(request.getProductId()))
                .and(ProductSpecification.vendorEqual(vendorExists.get()));

        Page<Product> products = productRepository.findAll(spec, PageRequest.of(request.getPage(), request.getSize(), Sort.by(Sort.Direction.DESC, "dateCreated")));

        PaginatedResponse<List<ProductDto>> paginatedResponse = new PaginatedResponse<>();
        paginatedResponse.setPage(products.getNumber());
        paginatedResponse.setSize(products.getSize());
        paginatedResponse.setTotal((int) productRepository.count());
        paginatedResponse.setData(convertProductListToDto(products.getContent()));
        return paginatedResponse;
    }


    @Override
    @PreAuthorize("hasAuthority('VENDOR')")
    public PaginatedResponse<List<ProductDto>> fetchAllProductsBy(ProductFilterRequest request) {
        Specification<Product> spec = Specification.where(
                        ProductSpecification.nameEqual(request.getName()))
                .and(ProductSpecification.categoryIn(request.getCategories())) // Use categoryIn for multiple categories
                .and(ProductSpecification.codeEqual(request.getCode()))
                .and(ProductSpecification.productIdEqual(request.getProductId()))
                .and(ProductSpecification.priceBetween(request.getMinPrice(), request.getMaxPrice())); // Add price range filter

        Page<Product> products = productRepository.findAll(spec, PageRequest.of(request.getPage(), request.getSize(), Sort.by(Sort.Direction.DESC, "dateCreated")));

        PaginatedResponse<List<ProductDto>> paginatedResponse = new PaginatedResponse<>();
        paginatedResponse.setPage(products.getNumber());
        paginatedResponse.setSize(products.getSize());
        paginatedResponse.setTotal((int) productRepository.count());
        paginatedResponse.setData(convertProductListToDto(products.getContent()));
        return paginatedResponse;
    }


    @Override
    public PaginatedResponse<List<ProductDto>> fetchAllProductsByAuth(ProductFilterRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<UserEntity> customer = userRepository.findByEmailAddress(username);

        if(customer.isEmpty()){
            throw new StitchException("User does not exist");
        }

        Specification<Product> spec = Specification.where(
                        ProductSpecification.nameEqual(request.getName()))
                .and(ProductSpecification.categoryIn(request.getCategories()))
                .and(ProductSpecification.codeEqual(request.getCode()))
                .and(ProductSpecification.productIdEqual(request.getProductId()));

        Page<Product> products = productRepository.findAll(spec, PageRequest.of(request.getPage(), request.getSize(), Sort.by(Sort.Direction.DESC, "dateCreated")));

        Pageable pageRequest = PageRequest.of(request.getPage(), request.getSize());

        Page<ProductLike> productLikesPage = productLikeRepository.findProductLikesByUserEntity(customer.get(), pageRequest);
        // to return true and a flag for every liked product
        PaginatedResponse<List<ProductDto>> paginatedResponse = new PaginatedResponse<>();
        paginatedResponse.setPage(products.getNumber());
        paginatedResponse.setSize(products.getSize());
        paginatedResponse.setTotal((int) productRepository.count());
        paginatedResponse.setData(convertProductListToDtoAndSortProductLikes(products.getContent(), productLikesPage.getContent()));
        return paginatedResponse;
    }


    public List<ProductDto> convertProductListToDtoAndSortProductLikes(List<Product> productList, List<ProductLike> productLikes){

        List<ProductDto> productDtoList = new ArrayList<>();

        for(Product product: productList){
            ProductDto productDto = convertProductToDto(product);
            for(ProductLike productLike: productLikes){
                if(productLike.getProductId().equals(product.getProductId())){
                    productDto.setLiked(true);

                    UserDto userDto = new UserDto();
                    userDto.setEmailAddress(product.getVendor().getEmailAddress());
                    userDto.setLastName(product.getVendor().getLastName());
                    userDto.setFirstName(product.getVendor().getFirstName());
                    productDto.setVendor(userDto);
                }
            }
            productDtoList.add(productDto);
        }
        return productDtoList;
    }

    public ProductDto convertProductToDto(Product product){
        log.info("products id----->>>> :{}", product.getProductId());

        ProductDto productDto = new ProductDto();

        BeanUtils.copyProperties(product, productDto);
        UserDto userDto = new UserDto();
        userDto.setEmailAddress(product.getVendor().getEmailAddress());
        userDto.setLastName(product.getVendor().getLastName());
        userDto.setFirstName(product.getVendor().getFirstName());
        productDto.setVendor(userDto);
        productDto.setProductVariation(convertProductVariationListToDto(productVariationRepository.findProductVariationByProductId(product.getProductId())));
        return productDto;
    }

    public List<ProductVariationDto> convertProductVariationListToDto(List<ProductVariation> products){
        log.info("products var----->>>> :{}", products);
        return products.stream().map(productVariation -> {
            ProductVariationDto productVariationDto = new ProductVariationDto();
            productVariationDto.setColor(productVariation.getColor());
            productVariationDto.setSleeveType(productVariation.getSleeveType());
            return productVariationDto;
        }).collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasAuthority('VENDOR')")
    public boolean togglePublishProduct(String productId) {
        System.out.println(productId);

        boolean publishedStatus = false;

        Optional <Product> existingProduct = productRepository.findByProductId(productId);
        if(existingProduct.isEmpty()){
            throw new UserException("product with id : " + productId + " is not found");
        }
        Product product = existingProduct.get();

        if(product.getPublishStatus().equals(PublishStatus.PUBLISHED)){
            product.setPublishStatus(PublishStatus.UNPUBLISHED);
            productRepository.save(product);
        }else {
            product.setPublishStatus(PublishStatus.PUBLISHED);
            productRepository.save(product);
            publishedStatus = true;
        }
        log.info("product info : {}", product);
        return publishedStatus;
    }
}
