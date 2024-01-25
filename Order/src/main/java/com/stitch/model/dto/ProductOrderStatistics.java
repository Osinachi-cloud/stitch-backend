package com.stitch.model.dto;


import lombok.Data;

@Data
public class ProductOrderStatistics {
    private long allOrdersCount;
    private long processingOrdersCount;
    private long cancelledOrdersCount;
    private long failedOrdersCount;
    private long completedOrdersCount;
}
