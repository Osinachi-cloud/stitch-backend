package com.stitch.utils;

import com.stitch.model.dto.ProductOrderDto;
import com.stitch.model.dto.ProductOrderRequest;
import com.stitch.model.entity.ProductOrder;
import com.stitch.model.enums.OrderStatus;
import org.springframework.beans.BeanUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Utils {


    public static List<ProductOrderDto> orderListToDto(List<ProductOrder> productOrderList) {

        List<ProductOrderDto> productOrderDtoList = new ArrayList<>();

        for(ProductOrder productOrder : productOrderList){
//            ProductOrderDto productOrderDto = new ProductOrderDto();

//            BeanUtils.copyProperties(productOrder, productOrderDto);

            productOrderDtoList.add(convertProductOrderToDto(productOrder));

        }
        return productOrderDtoList;

    }


    public static ProductOrderDto convertProductOrderToDto(ProductOrder productOrder) {
        ProductOrderDto productOrderDto = new ProductOrderDto();
        productOrderDto.setStatus(productOrder.getStatus());
        productOrderDto.setReferenceNumber(productOrder.getOrderId());
//        productOrderDto.setTransactionId(productOrder.getTransactionId());
//        productOrderDto.setMessage(productOrder.getMessage());
//        productOrderDto.setClientSecret(productOrder.getClientSecret());
//        productOrderDto.setPaymentId(productOrder.getPaymentId());
        productOrderDto.setProductCategoryName(productOrder.getProductCategoryName());
        productOrderDto.setVendorId(productOrder.getVendorId());
        productOrderDto.setOrderId(productOrder.getOrderId());
        productOrderDto.setCustomerId(productOrder.getCustomerId());
        productOrderDto.setCurrency(productOrder.getCurrency());
        productOrderDto.setPaymentMode(productOrder.getPaymentMode());
        productOrderDto.setAmount(productOrder.getAmount());
        productOrderDto.setCustomerId(productOrder.getCustomerId());
        productOrderDto.setDateCreated(formattedDate(productOrder.getDateCreated()));
        return productOrderDto;
    }

    public static String formattedDate(Instant dateCreated){

        try{
            Instant instant = Instant.parse(dateCreated.toString());
            ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("UTC"));
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy - hh:mm a");
            return zonedDateTime.format(outputFormatter);
        }catch (Exception exception){
            throw new RuntimeException(exception.getMessage());
        }
    }

    public static ProductOrder convertDtoToProductOrder(ProductOrderDto productOrderDto) {
        ProductOrder productOrder = new ProductOrder();
        productOrder.setStatus(productOrderDto.getStatus());
        productOrder.setOrderId(productOrderDto.getReferenceNumber());
//        productOrder.setTransactionId(productOrderDto.getTransactionId());
//        productOrder.setMessage(productOrderDto.getMessage());
//        productOrder.setClientSecret(productOrderDto.getClientSecret());
//        productOrder.setPaymentId(productOrderDto.getPaymentId());
        productOrder.setProductCategoryName(productOrderDto.getProductCategoryName());
        productOrder.setVendorId(productOrderDto.getVendorId());
        productOrder.setPaymentMode(productOrderDto.getPaymentMode());
        productOrder.setAmount(productOrderDto.getAmount());
        productOrder.setCustomerId(productOrderDto.getCustomerId());
        // Set other fields accordingly
        return productOrder;
    }

    public static ProductOrder convertRequestToModel(ProductOrderRequest productOrderRequest) {
        ProductOrder productOrder = new ProductOrder();
        productOrder.setOrderId(productOrderRequest.getOrderId());
        productOrder.setCustomerId(productOrderRequest.getCustomerId());
        productOrder.setProductId(productOrderRequest.getProductId());
        productOrder.setProductCategoryName(productOrderRequest.getProductCategoryName());
        productOrder.setVendorId(productOrderRequest.getVendorId());
        productOrder.setPaymentMode(productOrderRequest.getPaymentMode());
        productOrder.setCurrency(productOrderRequest.getCurrency());
        productOrder.setAmount(productOrderRequest.getAmount());
        productOrder.setStatus(OrderStatus.valueOf(productOrderRequest.getStatus()));
        productOrder.setNarration(productOrderRequest.getNarration());
        return productOrder;
    }

}
