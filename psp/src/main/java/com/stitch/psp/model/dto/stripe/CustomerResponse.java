package com.exquisapps.billanted.psp.model.dto.stripe;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CustomerResponse {

    @JsonProperty("id")
    private String id;
    @JsonProperty("object")
    private String object;
    @JsonProperty("address")
    private Object address;
    @JsonProperty("balance")
    private Integer balance;
    @JsonProperty("created")
    private Integer created;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("default_source")
    private Object defaultSource;
    @JsonProperty("delinquent")
    private Boolean delinquent;
    @JsonProperty("description")
    private String description;
    @JsonProperty("discount")
    private Object discount;
    @JsonProperty("email")
    private Object email;
    @JsonProperty("invoice_prefix")
    private String invoicePrefix;
    @JsonProperty("invoice_settings")
    private InvoiceSettings invoiceSettings;
    @JsonProperty("livemode")
    private Boolean livemode;
    @JsonProperty("name")
    private Object name;
    @JsonProperty("phone")
    private Object phone;
    @JsonProperty("preferred_locales")
    private List<Object> preferredLocales;
    @JsonProperty("shipping")
    private Object shipping;
    @JsonProperty("tax_exempt")
    private String taxExempt;
    @JsonProperty("test_clock")
    private Object testClock;


    @Data
    public static class InvoiceSettings {

        @JsonProperty("custom_fields")
        private Object customFields;
        @JsonProperty("default_payment_method")
        private Object defaultPaymentMethod;
        @JsonProperty("footer")
        private Object footer;
        @JsonProperty("rendering_options")
        private Object renderingOptions;
    }
}
