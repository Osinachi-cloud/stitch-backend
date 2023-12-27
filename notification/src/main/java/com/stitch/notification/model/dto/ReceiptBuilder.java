package com.stitch.notification.model.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ReceiptBuilder {
    private String transactionId;
    private String date;
    private String amount;
    private String billType;
    private String electricityToken;
    private String units;
    private String email;
    private String firstName;
}
