package com.stitch.model.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BillPaymentResponse {

    @JsonProperty("code")
    private String code;

    @JsonProperty("response_description")
    private String responseDescription;

    @JsonProperty("requestId")
    private String requestId;
}
