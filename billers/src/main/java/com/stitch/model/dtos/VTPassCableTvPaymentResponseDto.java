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
public class VTPassCableTvPaymentResponseDto extends BillPaymentResponse {

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
        @JsonProperty("channel")
        private String channel;
        @JsonProperty("transactionId")
        private String transactionId;
        @JsonProperty("method")
        private String method;
        @JsonProperty("platform")
        private String platform;
        @JsonProperty("is_api")
        private int isApi;
        @JsonProperty("discount")
        private String discount;
        @JsonProperty("user_id")
        private long userId;
        @JsonProperty("email")
        private String email;
        @JsonProperty("phone")
        private String phone;
        @JsonProperty("type")
        private String type;
        @JsonProperty("convinience_fee")
        private double convenienceFee;
        @JsonProperty("commission")
        private double commission;
        @JsonProperty("amount")
        private String amount;
        @JsonProperty("total_amount")
        private double totalAmount;
        @JsonProperty("quantity")
        private int quantity;
        @JsonProperty("unit_price")
        private String unitPrice;
        @JsonProperty("updated_at")
        private String updatedAt;
        @JsonProperty("created_at")
        private String createdAt;
        @JsonProperty("id")
        private long id;
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
        return "VTPassCableTvPaymentResponseDto{" +
                "content=" + content +
                ", amount='" + amount + '\'' +
                ", transactionDate=" + transactionDate +
                ", purchasedCode='" + purchasedCode + '\'' +
                "} " + super.toString();
    }
}
