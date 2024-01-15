package com.stitch.utils;

import com.stitch.model.dto.ProductOrderDto;
import com.stitch.model.entity.ProductOrder;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class Utils {


    public static List<ProductOrderDto> orderListToDto(List<ProductOrder> productOrderList) {

        List<ProductOrderDto> productOrderDtoList = new ArrayList<>();

        for(ProductOrder productOrder : productOrderList){
            ProductOrderDto productOrderDto = new ProductOrderDto();
            BeanUtils.copyProperties(productOrder, productOrderDto);

            productOrderDtoList.add(productOrderDto);

        }
        return productOrderDtoList;

    }
}
