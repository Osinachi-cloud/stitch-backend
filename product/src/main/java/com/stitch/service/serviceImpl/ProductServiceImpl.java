package com.stitch.service.serviceImpl;

import com.stitch.ProductSpecification;
import com.stitch.commons.model.dto.PaginatedResponse;
import com.stitch.commons.model.dto.Response;
import com.stitch.commons.util.NumberUtils;
import com.stitch.exception.ProductException;
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
import jakarta.transaction.Transactional;
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
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.stitch.commons.util.ResponseUtils.createSuccessResponse;
import static com.stitch.commons.util.SharedUtils.getLoggedInUser;
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
    public ProductDto createProduct(ProductRequest productRequest) {

        log.info("create product request  : {}", productRequest);
        try {
            validateProductRequest(productRequest);
            String email = getLoggedInUser().orElseThrow(() -> new ProductException("Failed to authenticate user", 403));

            Optional<UserEntity> customerExist = userRepository.findByEmailAddress(email);
            if (customerExist.isEmpty()) {
                throw new ProductException("Vendor with : " + email + " does not exist", 404);
            }

            // Check for duplicate code BEFORE attempting save
            if (productRepository.existsByCode(productRequest.getCode())) {
                throw new ProductException(
                    "A product with code '" + productRequest.getCode() + "' already exists. " +
                    "Please use a different product code and try again.",
                    HttpStatus.CONFLICT.value()
                );
            }

            UserEntity customer = customerExist.get();

            Product product = new Product();
            String prodId = NumberUtils.generate(10);
            product.setProductId(prodId);
            product.setCategory(ProductCategory.valueOf(productRequest.getCategory()));
            product.setFixedPrice(productRequest.isFixedPrice());

            BigDecimal price = productRequest.getPrice() != null ? productRequest.getPrice() : BigDecimal.ZERO;
            BigDecimal quantity = productRequest.getQuantity() != null ? productRequest.getQuantity() : BigDecimal.ZERO;
            BigDecimal discount = productRequest.getDiscount() != null ? productRequest.getDiscount() : BigDecimal.ZERO;

            product.setQuantity(quantity);
            product.setPrice(price);
            product.setName(productRequest.getName());
            product.setCode(productRequest.getCode());
            product.setShortDescription(productRequest.getShortDescription());
            product.setLongDescription(productRequest.getLongDescription());

            // Treat discount as flat amount (Naira) since frontend labels it as "Discount (₦)"
            BigDecimal sellingPrice = price.subtract(discount);
            if (sellingPrice.compareTo(BigDecimal.ZERO) < 0) {
                sellingPrice = BigDecimal.ZERO;
            }
            product.setSellingPrice(sellingPrice);

            product.setMaterialUsed(productRequest.getMaterialUsed());
            product.setReadyIn(productRequest.getReadyIn());
            product.setDiscount(discount);
            product.setVendor(customer);

            log.info("productRequest.getProductVariationDtoList() : {}", productRequest.getProductVariation());

            List<ProductVariation> productVariationList = convertProductVariationDtoListToEntity(productRequest.getProductVariation());
            product.setProductVariation(productVariationList);

            product.setPublishStatus(PublishStatus.valueOf(productRequest.getPublishStatus()));

            // Only process image if it's a non-empty base64 string or URL
            if (StringUtils.isNotBlank(productRequest.getProductImage())) {
                String image = productRequest.getProductImage().trim();
                // Only base64-decode if it looks like base64 (no http:// or https:// prefix)
                if (!image.startsWith("http://") && !image.startsWith("https://")) {
                    try {
                        byte[] imageBytes = Base64.decodeBase64(image);
                        String base64EncodedImage = Base64.encodeBase64String(imageBytes);
                        product.setProductImage(base64EncodedImage);
                    } catch (Exception imgEx) {
                        log.warn("Failed to base64-decode product image, storing as-is: {}", imgEx.getMessage());
                        product.setProductImage(image);
                    }
                } else {
                    product.setProductImage(image);
                }
            }

            Product savedProduct = productRepository.save(product);
            log.info("saved Product : {}", savedProduct);

            return convertProductToDto(savedProduct);
        } catch (ProductException e) {
            log.error("Custom exception occurred during product creation :: {}", e.getMessage());
            throw new ProductException(e.getMessage(), e.getCode());
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            String fullError = getFullExceptionMessage(e);
            log.error("Data integrity violation while creating product: {}", fullError);
            if (containsProductPkeyError(fullError)) {
                throw new ProductException(
                    "System error: The database ID sequence is out of sync. " +
                    "Please contact support or try again in a moment.",
                    HttpStatus.CONFLICT.value()
                );
            }
            throw new ProductException(
                "A product with code '" + productRequest.getCode() + "' already exists or conflicts with an existing record. " +
                "Please use a unique product code and try again.",
                HttpStatus.CONFLICT.value()
            );
        } catch (Exception e) {
            log.error("An error occurred while creating product", e);
            throw new ProductException("Unable to create product at this time. Please check your internet connection and try again.", 500);
        }
    }

    private String getFullExceptionMessage(Throwable e) {
        StringBuilder sb = new StringBuilder();
        Throwable current = e;
        while (current != null) {
            if (current.getMessage() != null) {
                sb.append(current.getMessage()).append(" ");
            }
            current = current.getCause();
        }
        return sb.toString().toLowerCase();
    }

    private boolean containsProductPkeyError(String message) {
        return message.contains("product_pkey") || message.contains("duplicate key value violates unique constraint");
    }


    public void validateProductRequest(ProductRequest productRequest) {

        if (!Objects.nonNull(productRequest)) {
            throw new ProductException("product request can not be null", 400);
        }
        if (StringUtils.isBlank(productRequest.getName())
                || StringUtils.isBlank(productRequest.getCode())
                || StringUtils.isBlank(productRequest.getCategory())) {
            throw new ProductException("Invalid request: Product name, code and category required", 400);
        }

    }

    @Override
    @PreAuthorize("hasAuthority('VENDOR')")
    public ProductDto updateProduct(ProductUpdateRequest productRequest, String productId) {
        try {
            Product product = productRepository.findByProductId(productId)
                    .orElseThrow(() -> new ProductException("Product with id: " + productId + "  can not be found", HttpStatus.NOT_FOUND.value()));
            product.setProductImage(productRequest.getProductImage());
            product.setCategory(ProductCategory.valueOf(productRequest.getCategory()));
            product.setFixedPrice(productRequest.isFixedPrice());
            product.setQuantity(productRequest.getQuantity());
            product.setPrice(productRequest.getAmount());
            product.setName(productRequest.getName());
            product.setCode(productRequest.getCode());
            Product updatedProduct = productRepository.save(product);
            return convertProductToDto(updatedProduct);
        } catch (ProductException e) {
            log.error("Custom error in update product :: {}", e.getMessage());
            throw new ProductException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            log.error("An error occurred while updating product {}", e.getMessage());
            throw new ProductException("Failed to update product", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public Response updateProductProfileImage(String profileImage, String productId) {
        try {
            Product product = productRepository.findByProductId(productId)
                    .orElseThrow(() -> new ProductException("Product with id: " + productId + "  can not be found", 404));
            product.setProductImage(profileImage);
            productRepository.save(product);
            return createSuccessResponse("product image updated successfully");
        } catch (Exception e) {
            log.error("An exception occurred while updating product image {}", e.getMessage());
            int code = e instanceof ProductException ? ((ProductException) e).getCode() : 500;
            String message = e instanceof ProductException ? e.getMessage() : "Failed to update product image";
            throw new ProductException(message, code);
        }
    }

    @Override
    public ProductDto getProductByProductId(String productId) {
        try {
            Product product = productRepository.findByProductId(productId).orElseThrow(
                    () -> new ProductException("Product with id: " + productId + "  can not be found", 404));
            return convertProductToDto(product);
        } catch (ProductException e) {
            log.error("Custom error in getting products by id {} : {}", productId, e.getMessage());
            throw new ProductException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            log.error("An error occurred while getting product by id {} : {}", productId, e.getMessage());
            throw new ProductException("Failed to get the product by id " + productId, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

    }

    @Override

    public PaginatedResponse<List<ProductDto>> getProductByVendor(String vendorId, int page, int size) {

        log.info(" vendor ID : {} \n page : {} \n size : {}", vendorId, page, size);

        try {
            PageRequest pageable = PageRequest.of(page, size);
            UserEntity customer = userService.getCustomerEntity(vendorId);

            Page<Product> productPage = productRepository.findProductsByVendor(customer, pageable);
            List<Product> productList = productPage.getContent();

            log.info(" productList : {}", vendorId);

            PaginatedResponse<List<ProductDto>> paginatedResponse = new PaginatedResponse<>();

            paginatedResponse.setSize(productPage.getSize());
            paginatedResponse.setData(convertProductListToDto(productList));
            paginatedResponse.setTotal(toIntExact(productPage.getTotalElements()));
            paginatedResponse.setPage(productPage.getNumber());
            return paginatedResponse;
        } catch (Exception e) {
            throw new ProductException("Failed to get products by vendor with id : " + vendorId, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

    }

    @Override
    @Transactional
    public Map<String, String> deleteProduct(String productId) {
        try {
            productRepository.deleteByProductId(productId);
            return Map.of("response", "DELETE SUCCESS");
        } catch (Exception e) {
            log.error("Failed to delete product Id: {} => {}", productId, e.getMessage());
            throw new ProductException("Failed to delete product Id: {}" + productId, 400);

        }
    }

    @Override
    public PaginatedResponse<List<ProductDto>> fetchAllProductsByVendor(ProductFilterRequest request) {

        try {
            String emailAddress = getLoggedInUser()
                    .orElseThrow(() -> new ProductException("Failed to authenticate user ", 403));
            UserEntity vendor = userRepository.findByEmailAddress(emailAddress)
                    .orElseThrow(() -> new ProductException("Vendor with Id: " + request.getVendorId() + " does not exist", 404));
            Specification<Product> spec = Specification.where(
                            ProductSpecification.nameEqual(request.getName()))
                    .and(ProductSpecification.categoryIn(request.getCategories()))
                    .and(ProductSpecification.codeEqual(request.getCode()))
                    .and(ProductSpecification.productIdEqual(request.getProductId()))
                    .and(ProductSpecification.vendorEqual(vendor));
            return mapProducts(spec, request);

        } catch (ProductException e) {
            log.error("Custom error in fetching vendor products : {}", e.getMessage());
            throw new ProductException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            log.error("An exception occurred while fetching products by vendor : {}", e.getMessage());
            throw new ProductException("Failed to fetch products by vendor ", 400);

        }

    }


    @Override
//    @PreAuthorize("hasAuthority('VENDOR')")
    public PaginatedResponse<List<ProductDto>> fetchAllProductsBy(ProductFilterRequest request) {
        try {
            Specification<Product> spec = Specification.where(
                            ProductSpecification.nameLike(request.getName()))
                    .and(ProductSpecification.shortDescriptionLike(request.getName()))
                    .and(ProductSpecification.vendorNameLike(request.getName()))
                    .and(ProductSpecification.categoryIn(request.getCategories()))
                    .and(ProductSpecification.codeEqual(request.getCode()))
                    .and(ProductSpecification.productIdEqual(request.getProductId()))
                    .and(ProductSpecification.priceBetween(request.getMinPrice(), request.getMaxPrice()));
            return mapProducts(spec, request);
        } catch (Exception e) {
            log.error("An error occurred while fetching products : {}", e.getMessage());
            throw new ProductException("Failed to fetch products by vendor ", 400);
        }

    }

    private PaginatedResponse<List<ProductDto>> mapProducts(Specification<Product> spec, ProductFilterRequest request) {
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

        try {
            String username = getLoggedInUser()
                    .orElseThrow(() -> new ProductException("Failed to authenticate user ", 403));

            UserEntity customer = userRepository.findByEmailAddress(username)
                    .orElseThrow(() -> new ProductException("Customer with email: " + username + " does not exist", 404));


            Specification<Product> spec = Specification.where(
                            ProductSpecification.nameLike(request.getName()))
                    .and(ProductSpecification.shortDescriptionLike(request.getName()))
                    .and(ProductSpecification.vendorNameLike(request.getName()))
                    .and(ProductSpecification.categoryIn(request.getCategories()))
                    .and(ProductSpecification.codeEqual(request.getCode()))
                    .and(ProductSpecification.productIdEqual(request.getProductId()))
                    .and(ProductSpecification.priceBetween(request.getMinPrice(), request.getMaxPrice()));

            Page<Product> products = productRepository.findAll(spec, PageRequest.of(request.getPage(), request.getSize(), Sort.by(Sort.Direction.DESC, "dateCreated")));

            Pageable pageRequest = PageRequest.of(request.getPage(), request.getSize());

            Page<ProductLike> productLikesPage = productLikeRepository.findProductLikesByUserEntity(customer, pageRequest);
            PaginatedResponse<List<ProductDto>> paginatedResponse = new PaginatedResponse<>();
            paginatedResponse.setPage(products.getNumber());
            paginatedResponse.setSize(products.getSize());
            paginatedResponse.setTotal((int) productRepository.count());
            paginatedResponse.setData(convertProductListToDtoAndSortProductLikes(products.getContent(), productLikesPage.getContent()));
            return paginatedResponse;
        } catch (ProductException e) {
            log.error("Custom error in fetching all products : {}", e.getMessage());
            throw new ProductException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            log.error("An exception occurred while fetching all products : {}", e.getMessage());
            throw new ProductException("Failed to fetch all products ", 400);
        }

    }


    public List<ProductDto> convertProductListToDtoAndSortProductLikes(List<Product> productList, List<ProductLike> productLikes) {

        List<ProductDto> productDtoList = new ArrayList<>();

        for (Product product : productList) {
            ProductDto productDto = convertProductToDto(product);
            for (ProductLike productLike : productLikes) {
                if (productLike.getProductId().equals(product.getProductId())) {
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

    public ProductDto convertProductToDto(Product product) {
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

    public List<ProductVariationDto> convertProductVariationListToDto(List<ProductVariation> products) {
        log.info("products var----->>>> :{}", products);
        return products.stream().map(productVariation -> {
            ProductVariationDto productVariationDto = new ProductVariationDto();
            productVariationDto.setColor(productVariation.getColor());
            productVariationDto.setSleeveType(productVariation.getSleeveType());
            return productVariationDto;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean togglePublishProduct(String productId) {
        System.out.println(productId);

        boolean publishedStatus = false;

        Optional<Product> existingProduct = productRepository.findByProductId(productId);
        if (existingProduct.isEmpty()) {
            throw new UserException("product with id : " + productId + " is not found");
        }
        Product product = existingProduct.get();
        if (product.getPublishStatus().equals(PublishStatus.PUBLISHED)) {
            product.setPublishStatus(PublishStatus.UNPUBLISHED);
            productRepository.save(product);
        } else {
            product.setPublishStatus(PublishStatus.PUBLISHED);
            productRepository.save(product);
            publishedStatus = true;
        }
        log.info("product info : {}", product);
        return publishedStatus;
    }
}
