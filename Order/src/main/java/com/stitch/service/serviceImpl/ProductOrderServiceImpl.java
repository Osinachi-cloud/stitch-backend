package com.stitch.service.serviceImpl;

import com.stitch.commons.model.dto.PaginatedResponse;
import com.stitch.model.dto.ProductOrderDto;
import com.stitch.model.entity.ProductOrder;
import com.stitch.repository.ProductOrderRepository;
import com.stitch.service.ProductOrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.stitch.utils.Utils.orderListToDto;
import static java.lang.Math.toIntExact;

@Service
public class ProductOrderServiceImpl implements ProductOrderService {

    private final ProductOrderRepository productOrderRepository;

    public ProductOrderServiceImpl(ProductOrderRepository productOrderRepository) {
        this.productOrderRepository = productOrderRepository;
    }

    @Override
    public PaginatedResponse<List<ProductOrderDto>> fetchCustomerOrdersBy(String productId, String status, String orderId, String productCategory, String vendorId, PageRequest pr) {

        Page<ProductOrder> orderPage = productOrderRepository.fetchCustomerOrdersBy(productId, status, orderId, productCategory,vendorId, pr);

        List<ProductOrder> productOrderList = orderPage.getContent();
        List<ProductOrderDto> productOrderDtoList = orderListToDto(productOrderList);

        List<ProductOrderDto> productOrderDtoListRes = new ArrayList<>();

//        for(OrderDto orderDto: orderDtoList){
//            List<Wallet> walletList = walletRepository.findByCustomerId(customerDto.getCustomerId());
//
//            if (!walletList.isEmpty()){
//                customerDto.setWalletBalance(walletList.get(0).getBalance());
//            };
//            customerDtoListRes.add(customerDto);
//        }
        PaginatedResponse<List<ProductOrderDto>> paginatedResponse = new PaginatedResponse<>();
        paginatedResponse.setPage(orderPage.getNumber());
        paginatedResponse.setData(productOrderDtoListRes);
        paginatedResponse.setSize(orderPage.getSize());
        paginatedResponse.setTotal(toIntExact(orderPage.getTotalElements()));

        return paginatedResponse;

    }
}
