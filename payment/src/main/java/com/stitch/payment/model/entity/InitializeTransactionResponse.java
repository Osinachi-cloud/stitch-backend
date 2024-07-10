package com.stitch.payment.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Response returned from the paystack initialize transaction api
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class InitializeTransactionResponse {

    @JsonProperty("status")
    private boolean status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private Data data;

//    @JsonProperty("reference")
//    private String reference;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @lombok.Data
    public class Data {
        /**
         * this is the redirect url that the user would use to make the payment
         */
        @JsonProperty("authorization_url")
        private String authorizationUrl;
        /**
         * this code identifies the payment url
         */

        @JsonProperty("access_code")
        private String accessCode;
        /**
         * the unique reference used to identify this transaction
         */
        @JsonProperty("reference")
        private String reference;

    }

}