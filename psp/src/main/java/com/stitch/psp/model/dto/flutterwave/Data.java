package com.exquisapps.billanted.psp.model.dto.flutterwave;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashMap;
import java.util.Map;

@lombok.Data
public class Data {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("tx_ref")
    private String txRef;
    @JsonProperty("flw_ref")
    private String flwRef;
    @JsonProperty("device_fingerprint")
    private String deviceFingerprint;
    @JsonProperty("amount")
    private Double amount;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("charged_amount")
    private Integer chargedAmount;
    @JsonProperty("app_fee")
    private Double appFee;
    @JsonProperty("merchant_fee")
    private Integer merchantFee;
    @JsonProperty("processor_response")
    private String processorResponse;
    @JsonProperty("auth_model")
    private String authModel;
    @JsonProperty("ip")
    private String ip;
    @JsonProperty("narration")
    private String narration;
    @JsonProperty("status")
    private String status;
    @JsonProperty("payment_type")
    private String paymentType;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("account_id")
    private Integer accountId;
    @JsonProperty("card")
    private Card card;
    @JsonProperty("meta")
    private Meta meta;
    @JsonProperty("amount_settled")
    private Double amountSettled;
    @JsonProperty("customer")
    private Customer customer;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

}
