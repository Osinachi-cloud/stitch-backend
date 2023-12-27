package com.exquisapps.billanted.psp.model.dto.flutterwave;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;


@Data
class Meta {

    @JsonProperty("__CheckoutInitAddress")
    private String checkoutInitAddress;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

}
