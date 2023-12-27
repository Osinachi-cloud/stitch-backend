package com.exquisapps.billanted.psp.model.dto.flutterwave;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashMap;
import java.util.Map;

@lombok.Data
public class Card {
    @JsonProperty("first_6digits")
    private String first6digits;
    @JsonProperty("last_4digits")
    private String last4digits;
    @JsonProperty("issuer")
    private String issuer;
    @JsonProperty("country")
    private String country;
    @JsonProperty("type")
    private String type;
    @JsonProperty("token")
    private String token;
    @JsonProperty("expiry")
    private String expiry;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

}
