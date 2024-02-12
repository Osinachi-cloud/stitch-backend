package com.stitch.utils;

import com.stitch.model.dto.ProductDto;
import com.stitch.model.dto.ProductRequest;
import com.stitch.model.entity.Product;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class ProductUtils {

    public static ProductDto convertProductToDto(Product product){
        ProductDto productDto = new ProductDto();

        BeanUtils.copyProperties(product, productDto);
        return productDto;
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

}
