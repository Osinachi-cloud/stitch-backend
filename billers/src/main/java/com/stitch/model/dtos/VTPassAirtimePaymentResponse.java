package com.stitch.model.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class VTPassAirtimePaymentResponse extends BillPaymentResponse {


    @JsonProperty("content")
    private Content content;
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
        private Object serviceVerification; // Use appropriate type here
        @JsonProperty("channel")
        private String channel;
        @JsonProperty("commission")
        private double commission;
        @JsonProperty("total_amount")
        private double totalAmount;
        @JsonProperty("discount")
        private Object discount; // Use appropriate type here
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


    @Override
    public String toString() {
        return "VTPassAirtimePaymentResponse{" +
                "content=" + content +
                ", amount='" + amount + '\'' +
                ", transactionDate=" + transactionDate +
                ", purchasedCode='" + purchasedCode + '\'' +
                "} " + super.toString();
    }
}

