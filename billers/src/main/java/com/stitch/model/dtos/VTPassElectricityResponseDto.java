package com.stitch.model.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class VTPassElectricityResponseDto {


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
    @JsonProperty("mainToken")
    private String mainToken;
    @JsonProperty("mainTokenDescription")
    private String mainTokenDescription;
    @JsonProperty("mainTokenUnits")
    private Double mainTokenUnits;
    @JsonProperty("mainTokenTax")
    private Double mainTokenTax;
    @JsonProperty("mainsTokenAmount")
    private Double mainsTokenAmount;
    @JsonProperty("bonusToken")
    private String bonusToken;
    @JsonProperty("customerName")
    private String customerName;
    @JsonProperty("customerAddress")
    private String customerAddress;
    @JsonProperty("meterNumber")
    private String meterNumber;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Address")
    private String address;
    @JsonProperty("token")
    private String token;
    @JsonProperty("units")
    private String units;
    @JsonProperty("bonusTokenDescription")
    private String bonusTokenDescription;
    @JsonProperty("bonusTokenUnits")
    private Integer bonusTokenUnits;
    @JsonProperty("bonusTokenTax")
    private Double bonusTokenTax;
    @JsonProperty("bonusTokenAmount")
    private Double bonusTokenAmount;
    @JsonProperty("tariffIndex")
    private String tariffIndex;
    @JsonProperty("debtDescription")
    private String debtDescription;

    @Data
    public static class Content {
        @JsonProperty("transactions")
        private Transactions transactions;
    }

    @Data
    public static class Transactions {
        @JsonProperty("amount")
        private Integer amount;
        @JsonProperty("convinience_fee")
        private Integer convenienceFee;
        @JsonProperty("status")
        private String status;
        @JsonProperty("name")
        private String name;
        @JsonProperty("phone")
        private String phone;
        @JsonProperty("email")
        private String email;
        @JsonProperty("type")
        private String type;
        @JsonProperty("created_at")
        private String createdAt;
        @JsonProperty("discount")
        private Double discount;
        @JsonProperty("giftcard_id")
        private Double giftcardId;
        @JsonProperty("total_amount")
        private Integer totalAmount;
        @JsonProperty("commission")
        private Integer commission;
        @JsonProperty("channel")
        private String channel;
        @JsonProperty("platform")
        private String platform;
        @JsonProperty("service_verification")
        private Object serviceVerification;
        @JsonProperty("quantity")
        private Integer quantity;
        @JsonProperty("unit_price")
        private Integer unitPrice;
        @JsonProperty("unique_element")
        private String uniqueElement;
        @JsonProperty("product_name")
        private String productName;
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
