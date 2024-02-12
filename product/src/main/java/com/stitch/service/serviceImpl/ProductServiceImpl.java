package com.stitch.service.serviceImpl;

import com.stitch.commons.enums.ResponseStatus;
import com.stitch.commons.exception.StitchException;
import com.stitch.commons.model.dto.PaginatedResponse;
import com.stitch.commons.model.dto.Response;
import com.stitch.commons.util.NumberUtils;
import com.stitch.commons.util.ResponseUtils;
import com.stitch.model.ProductCategory;
import com.stitch.model.dto.ProductDto;
import com.stitch.model.dto.ProductRequest;
import com.stitch.model.dto.ProductUpdateRequest;
import com.stitch.model.entity.Product;
import com.stitch.repository.ProductRepository;
import com.stitch.service.ProductService;
import com.stitch.user.exception.UserException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.stitch.utils.ProductUtils.convertProductListToDto;
import static com.stitch.utils.ProductUtils.convertProductToDto;
import static java.lang.Math.toIntExact;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductDto createProduct(ProductRequest productRequest) {


        validateProductRequest(productRequest);

        Product product = new Product();
        product.setProductId(NumberUtils.generate(10));
        product.setProductImage(productRequest.getProductImage());
        product.setCategory(ProductCategory.valueOf(productRequest.getCategory()));
        product.setFixedPrice(productRequest.isFixedPrice());
        product.setQuantity(productRequest.getQuantity());
        product.setAmount(productRequest.getAmount());
        product.setName(productRequest.getName());
        product.setCode(productRequest.getCode());

        Product savedProduct = productRepository.save(product);

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

        Page<Product> productPage = productRepository.findProductsByVendorId(vendorId, pageable);
        List<Product> productList = productPage.getContent();

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


}
