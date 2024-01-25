package com.stitch.service.serviceImpl;

import com.stitch.commons.exception.StitchException;
import com.stitch.commons.model.dto.PaginatedResponse;
import com.stitch.model.dto.ProductOrderDto;
import com.stitch.model.dto.ProductOrderRequest;
import com.stitch.model.dto.ProductOrderStatistics;
import com.stitch.model.entity.ProductOrder;
import com.stitch.repository.ProductOrderRepository;
import com.stitch.service.ProductOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.stitch.commons.util.Mapper.convertModelToDto;
import static com.stitch.utils.Utils.*;
import static java.lang.Math.toIntExact;

@Service
public class ProductOrderServiceImpl implements ProductOrderService {

    private final Logger log = LoggerFactory.getLogger(ProductOrderServiceImpl.class);

    private final ProductOrderRepository productOrderRepository;

//    private final AdminService adminService;

    public ProductOrderServiceImpl(ProductOrderRepository productOrderRepository) {
        this.productOrderRepository = productOrderRepository;
    }

    @Override
    public PaginatedResponse<List<ProductOrderDto>> fetchCustomerOrdersBy(String productId, String customerId , String status, String orderId, String productCategory, String vendorId, PageRequest pr) {

        Page<ProductOrder> orderPage = productOrderRepository.fetchCustomerOrdersBy(productId, customerId, status, orderId, productCategory,vendorId, pr);

        List<ProductOrder> productOrderList = orderPage.getContent();
        log.info("productOrderList ====>>>  : {}", productOrderList);
        List<ProductOrderDto> productOrderDtoList = orderListToDto(productOrderList);

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
        paginatedResponse.setData(productOrderDtoList);
        paginatedResponse.setSize(orderPage.getSize());
        paginatedResponse.setTotal(toIntExact(orderPage.getTotalElements()));

        return paginatedResponse;

    }

    @Override
    public ProductOrderDto getProductOrder(String productOrderId){
        Optional<ProductOrder> existingProductOrder = productOrderRepository.findByProductId(productOrderId);

        if(existingProductOrder.isEmpty()){
            throw new StitchException("product order does not exist " + productOrderId);
        }
        ProductOrder productOrder = existingProductOrder.get();

        ProductOrderDto productOrderDto = new ProductOrderDto();

        return (ProductOrderDto) convertModelToDto(productOrder, productOrderDto);

    }

    @Override
    public ProductOrderDto createProductOrder(ProductOrderRequest productOrderDto){
        ProductOrder productOrder = convertRequestToModel(productOrderDto);
        ProductOrder savedproductOrder = productOrderRepository.save(productOrder);

        return convertProductOrderToDto(savedproductOrder);
    }


    @Override
    public ProductOrderStatistics getCustomerProductStat(String customerId){
        System.out.println("Got to stats method 2");

        List<ProductOrder> existingProductOrder = productOrderRepository.findByCustomerId(customerId);

        if(existingProductOrder.isEmpty()){
            throw new StitchException("customer with : " + customerId + " does not exist");
        }

        log.info("existingProductOrder ===> :{}", existingProductOrder);
        ProductOrderStatistics productOrderStatistics = new ProductOrderStatistics();
        productOrderStatistics.setAllOrdersCount(productOrderRepository.countAllOrdersByCustomerId(customerId));
        productOrderStatistics.setCompletedOrdersCount(productOrderRepository.countCompletedOrdersByCustomerId(customerId));
        productOrderStatistics.setCancelledOrdersCount(productOrderRepository.countCancelledOrdersByCustomerId(customerId));
        productOrderStatistics.setProcessingOrdersCount(productOrderRepository.countProcessingOrdersByCustomerId(customerId));
        productOrderStatistics.setFailedOrdersCount(productOrderRepository.countFailedOrdersByCustomerId(customerId));

        return productOrderStatistics;
    }

}
