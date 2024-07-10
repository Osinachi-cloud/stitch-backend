package com.stitch.payment.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class PaymentVerificationResponse {

    @JsonProperty("status")
    private Boolean status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private Data data;


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Data{

        @JsonProperty("id")
        private long id;

        @JsonProperty("domain")
        private String domain;

        @JsonProperty("authorization_url")
        private String authorizationUrl;

        @JsonProperty("status")
        private String status;

        @JsonProperty("reference")
        private String reference;

        @JsonProperty("access_code")
        private String accessCode;

        @JsonProperty("receipt_number")
        private String receiptNumber;

        @JsonProperty("amount")
        private BigDecimal amount;

        @JsonProperty("message")
        private String message;

        @JsonProperty("gateway_response")
        private String gatewayResponse;

        @JsonProperty("paid_at")
        private String paidAt;

        @JsonProperty("created_at")
        private String createdAt;

        @JsonProperty("channel")
        private String channel;

        @JsonProperty("currency")
        private String currency;

        @JsonProperty("ip_address")
        private String ipAddress;

        @JsonProperty("metadata")
        private String metadata;

        @JsonProperty("log")
        private String log;

        @JsonProperty("fees")
        private String fees;

        @JsonProperty("fees_split")
        private String feesSplit;

        @JsonProperty("authorization")
        private Object authorization;

        @JsonProperty("customer")
        private Customer customer;

        @JsonProperty("plan")
        private Object plan;

        @JsonProperty("split")
        private Object split;

        @JsonProperty("order_id")
        private String orderId;

        @JsonProperty("paidAt")
        private String paidAtAlternate;

        @JsonProperty("createdAt")
        private String createdAtAlternate;

        @JsonProperty("requested_amount")
        private int requestedAmount;

        @JsonProperty("pos_transaction_data")
        private String posTransactionData;

        @JsonProperty("source")
        private String source;

        @JsonProperty("fees_breakdown")
        private String feesBreakdown;

        @JsonProperty("connect")
        private String connect;

        @JsonProperty("transaction_date")
        private String transactionDate;

        @JsonProperty("plan_object")
        private PlanObject planObject;

        @JsonProperty("subaccount")
        private Subaccount subaccount;


            @Getter
            @Setter
            @AllArgsConstructor
            @NoArgsConstructor
            @JsonInclude(JsonInclude.Include.NON_NULL)
            @JsonIgnoreProperties(ignoreUnknown = true)
            public class Customer {

                @JsonProperty("id")
                private long id;

                @JsonProperty("first_name")
                private String firstName;

                @JsonProperty("last_name")
                private String lastName;

                @JsonProperty("email")
                private String email;

                @JsonProperty("customer_code")
                private String customerCode;

                @JsonProperty("phone")
                private String phone;

                @JsonProperty("metadata")
                private String metadata;

                @JsonProperty("risk_action")
                private String riskAction;

                @JsonProperty("international_format_phone")
                private String internationalFormatPhone;

            }

            public class PlanObject {

            }

            public class Subaccount {
            }

    }


}



