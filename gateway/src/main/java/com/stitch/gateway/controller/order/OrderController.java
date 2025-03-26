package com.stitch.gateway.controller.order;

import com.stitch.commons.model.dto.PaginatedResponse;
import com.stitch.model.dto.ProductOrderDto;
import com.stitch.model.dto.ProductOrderRequest;
import com.stitch.model.dto.ProductOrderStatistics;
import com.stitch.service.ProductOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.stitch.gateway.util.Constants.BASE_URL;


@RestController
@RequestMapping(BASE_URL)
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    private final ProductOrderService productOrderService;


    @GetMapping("/fetch-customer-orders")
    public ResponseEntity<PaginatedResponse<List<ProductOrderDto>>> fetchCustomerOrders(@RequestParam(required = false) String productId,
                                                                                        @RequestParam(required = false) String emailAddress, @RequestParam(required = false) String status,
                                                                                        @RequestParam(required = false) String orderId, @RequestParam(required = false) String productCategory,
                                                                                        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(productOrderService.fetchCustomerOrders(productId, emailAddress, status, orderId, productCategory, page, size));

    }


    @GetMapping("/fetch-vendor-orders")
    public ResponseEntity<PaginatedResponse<List<ProductOrderDto>>> fetchVendorOrders(@RequestParam(required = false) String productId,
                                                                                      @RequestParam(required = false) String emailAddress, @RequestParam(required = false) String status,
                                                                                      @RequestParam(required = false) String orderId, @RequestParam(required = false) String productCategory,
                                                                                      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(productOrderService.fetchVendorOrders(productId, emailAddress, status, orderId, productCategory, page, size));

    }

    @PostMapping("/create product -order")
    public ResponseEntity<ProductOrderDto> createProductOrder(@RequestBody ProductOrderRequest productOrderDto) {
        return ResponseEntity.ok(productOrderService.createProductOrder(productOrderDto));

    }


    @PostMapping("/update-order-status/{orderId}")
    public ResponseEntity<ProductOrderDto> updateProductOrderStatus(@PathVariable String orderId, @RequestParam(required = false) String orderStatus) {
        return ResponseEntity.ok(productOrderService.updateProductOrder(orderId, orderStatus));
    }

    @GetMapping("/order-stats-for-customer")
    public ResponseEntity<ProductOrderStatistics> getProductOrderStatsByCustomer() {
        return ResponseEntity.ok(productOrderService.getCustomerProductStat());
    }

    @GetMapping("/order-stats-for-vendor")
    @PreAuthorize("hasAuthority('VENDOR')")
    public ResponseEntity<ProductOrderStatistics> getProductOrderStatsByVendor() {
            return ResponseEntity.ok(productOrderService.getVendorProductStat());

    }

    @GetMapping(value = "getOrderByOrderId")
    @PreAuthorize("hasAuthority('VENDOR')")
    public ProductOrderDto getOrderByOrderId(@RequestParam("orderId") String orderId) {

        try {
            return productOrderService.getOrderByOrderId(orderId);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}