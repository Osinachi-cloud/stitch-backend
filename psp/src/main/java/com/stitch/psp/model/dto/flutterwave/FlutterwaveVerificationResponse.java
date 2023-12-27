package com.stitch.psp.model.dto.flutterwave;

import com.exquisapps.billanted.psp.model.dto.flutterwave.Data;
import com.fasterxml.jackson.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@lombok.Data
public class FlutterwaveVerificationResponse {

    @JsonProperty("status")
    private String status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private Data data;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();


}

