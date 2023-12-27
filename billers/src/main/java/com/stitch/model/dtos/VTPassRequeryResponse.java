package com.stitch.model.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VTPassRequeryResponse {

    @JsonProperty("code")
    private String code;
    @JsonProperty("content")
    private Content content;
    @JsonProperty("response_description")
    private String responseDescription;
    @JsonProperty("requestId")
    private String requestId;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("transaction_date")
    private TransactionDate transactionDate;
    @JsonProperty("purchased_code")
    private String purchasedCode;

    @Data
    public static class Content {
        @JsonProperty("transactions")
        private Transactions transactions;
    }

    @Data
    public static class Transactions {
        @JsonProperty("status")
        private String status;
        @JsonProperty("product_name")
        private String productName;
        @JsonProperty("unique_element")
        private String uniqueElement;
        @JsonProperty("unique_price")
        private double unitPrice;
        @JsonProperty("quantity")
        private int quantity;
        @JsonProperty("service_verification")
        private Object serviceVerification;
        @JsonProperty("channel")
        private String channel;
        @JsonProperty("commission")
        private double commission;
        @JsonProperty("total_amount")
        private double totalAmount;
        @JsonProperty("discount")
        private Object discount;
        @JsonProperty("type")
        private String type;
        @JsonProperty("email")
        private String email;
        @JsonProperty("phone")
        private String phone;
        @JsonProperty("name")
        private String name;
        @JsonProperty("convinience_fee")
        private double convenienceFee;
        @JsonProperty("amount")
        private double amount;
        @JsonProperty("platform")
        private String platform;
        @JsonProperty("method")
        private String method;
        @JsonProperty("transactionId")
        private String transactionId;
    }

    @Data
    public static class TransactionDate {
        @JsonProperty("date")
        private String date;
        @JsonProperty("timezone_type")
        private int timezoneType;
        @JsonProperty("timezone")
        private String timezone;
    }
}
