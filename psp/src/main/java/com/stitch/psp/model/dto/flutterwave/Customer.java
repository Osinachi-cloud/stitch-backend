package com.exquisapps.billanted.psp.model.dto.flutterwave;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
class Customer {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("email")
    private String email;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

}
