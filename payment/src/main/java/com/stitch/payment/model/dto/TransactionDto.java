package com.stitch.payment.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionDto {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("transactionType")
    private String transactionType;
    @JsonProperty("transactionId")
    private String transactionId;
    @JsonProperty("transactionPaymentOption")
    private TransactionPaymentOption transactionPaymentOption;
    @JsonProperty("description")
    private String description;
    @JsonProperty("timestamp")
    private String timestamp;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("sourceAmount")
    private String sourceAmount;
    @JsonProperty("sourceCurrency")
    private String sourceCurrency;
    @JsonProperty("status")
    private String status;
    @JsonProperty("orderId")
    private String orderId;
    @JsonProperty("fullName")
    private String fullName;
    @JsonProperty("utilityReceipt")
    private UtilityReceipt utilityReceipt;


    @Data
    @AllArgsConstructor
    public static class TransactionId {

        @JsonProperty("label")
        private String label;
        @JsonProperty("value")
        private String value;
    }

    @Data
    @AllArgsConstructor
    public static class TransactionPaymentOption {

        @JsonProperty("type")
        private String type;
        @JsonProperty("paymentOption")
        private String paymentOption;
        @JsonProperty("message")
        private String message;
    }

    @Data
    @AllArgsConstructor
    public static class TransactionType {

        @JsonProperty("label")
        private String label;
        @JsonProperty("value")
        private String value;
    }

    @Data
    @Builder
    public static class UtilityReceipt {

        @JsonProperty("paidOn")
        private PaidOn paidOn;
        @JsonProperty("transactionType")
        private TransactionType transactionType;
        @JsonProperty("billProvider")
        private BillProvider billProvider;
        @JsonProperty("customerUtilityId")
        private CustomerUtilityId customerUtilityId;
        @JsonProperty("customerUtilityName")
        private CustomerUtilityName customerUtilityName;
        @JsonProperty("amount")
        private Amount amount;
        @JsonProperty("transactionId")
        private TransactionId transactionId;
        @JsonProperty("address")
        private Address address;

        @JsonProperty("meterToken")
        private MeterToken meterToken;

        @JsonProperty("meterUnits")
        private MeterUnits meterUnits;
    }

    @Data
    @AllArgsConstructor
    public static class PaidOn {

        @JsonProperty("label")
        private String label;
        @JsonProperty("value")
        private String value;
    }

    @Data
    @AllArgsConstructor
    public static class CustomerUtilityName {

        @JsonProperty("label")
        private String label;
        @JsonProperty("value")
        private String value;
    }

    @Data
    @AllArgsConstructor
    public static class CustomerUtilityId {

        @JsonProperty("label")
        private String label;
        @JsonProperty("value")
        private String value;
    }

    @Data
    @AllArgsConstructor
    public static class BillProvider {

        @JsonProperty("label")
        private String label;
        @JsonProperty("value")
        private String value;
    }

    @Data
    @AllArgsConstructor
    public static class Amount {

        @JsonProperty("label")
        private String label;
        @JsonProperty("value")
        private String value;
    }

    @Data
    @AllArgsConstructor
    public static class Address {

        @JsonProperty("label")
        private String label;
        @JsonProperty("value")
        private String value;
    }

    @Data
    @AllArgsConstructor
    public static class MeterToken {

        @JsonProperty("label")
        private String label;
        @JsonProperty("value")
        private String value;
    }

    @Data
    @AllArgsConstructor
    public static class MeterUnits {

        @JsonProperty("label")
        private String label;
        @JsonProperty("value")
        private String value;
    }
}
