package com.stitch.utils;

import com.stitch.commons.exception.StitchException;
import com.stitch.model.dto.ProductDto;
import com.stitch.model.dto.ProductVariationDto;
import com.stitch.model.entity.Product;
import com.stitch.model.entity.ProductLike;
import com.stitch.model.entity.ProductVariation;
import com.stitch.repository.ProductVariationRepository;
import com.stitch.user.model.dto.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductUtils {

    @Autowired
    private ProductVariationRepository productVariationRepository;

    public static ProductDto convertProductToDto(Product product){
        ProductDto productDto = new ProductDto();

        BeanUtils.copyProperties(product, productDto);
        UserDto userDto = new UserDto();
        userDto.setEmailAddress(product.getVendor().getEmailAddress());
        userDto.setLastName(product.getVendor().getLastName());
        userDto.setFirstName(product.getVendor().getFirstName());
        productDto.setVendor(userDto);
        productDto.setProductVariation(convertProductVariationListToDto(product.getProductVariation()));


//        productDto.setColor(product.getProductVariation().getColor());
//        productDto.setSleeveStyle(product.getProductVariation().getSleeveStyle());
        return productDto;
    }

    public static ProductVariationDto convertProductVariationListToDto(ProductVariation product){
        ProductVariationDto productDto = new ProductVariationDto();
        BeanUtils.copyProperties(product, productDto);
        return productDto;
    }

    public static ProductVariation convertProductVariationListToDto(ProductVariationDto productDto){
        ProductVariation product = new ProductVariation();
        BeanUtils.copyProperties(productDto, product);
        return product;
    }

    public static List<ProductVariation> convertProductVariationDtoListToEntity(List<ProductVariationDto> productDtos){
        List<ProductVariation> products = new ArrayList<>();
        try{
            return productDtos.stream().map(productVariationDto -> {
                ProductVariation productVariation = new ProductVariation();
                productVariation.setColor(productVariationDto.getColor());
                productVariation.setSleeveType(productVariationDto.getSleeveType());
                return productVariation;
            }).collect(Collectors.toList());
        } catch (Exception e){
            throw new StitchException("Error: " + e.getMessage());
        }
    }

    public static List<ProductVariationDto> convertProductVariationListToDto(List<ProductVariation> products){
//        log.info("products :{}", products);
        return products.stream().map(productVariation -> {
            ProductVariationDto productVariationDto = new ProductVariationDto();
            productVariationDto.setColor(productVariation.getColor());
            productVariationDto.setSleeveType(productVariation.getSleeveType());
            return productVariationDto;
        }).collect(Collectors.toList());
    }

    public static List<ProductDto> convertProductListToDto(List<Product> productList){

        List<ProductDto> productDtoList = new ArrayList<>();
        for(Product product: productList){
            ProductDto productDto = new ProductDto();
            BeanUtils.copyProperties(product, productDto);
            productDtoList.add(productDto);
        }
        return productDtoList;
    }

//    public static List<ProductDto> convertProductListToDtoAndSortProductLikes(List<Product> productList, List<ProductLike> productLikes){
//
//        List<ProductDto> productDtoList = new ArrayList<>();
//
//        for(Product product: productList){
//            ProductDto productDto = new ProductDto();
//            BeanUtils.copyProperties(product, productDto);
//            for(ProductLike productLike: productLikes){
//                if(productLike.getProductId().equals(product.getProductId())){
//                    productDto.setLiked(true);
//
//                    UserDto userDto = new UserDto();
//                    userDto.setEmailAddress(product.getVendor().getEmailAddress());
//                    userDto.setLastName(product.getVendor().getLastName());
//                    userDto.setFirstName(product.getVendor().getFirstName());
//                    productDto.setVendor(userDto);
//                }
//            }
//            productDtoList.add(productDto);
//        }
//        return productDtoList;
//    }


}
