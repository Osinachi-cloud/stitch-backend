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
public class VTPassElectricityPrePaidResponseDto extends VTPassElectricityResponseDto{


    @JsonProperty("code")
    private String code;
    @JsonProperty("content")
    private Content content;
    @JsonProperty("response_description")
    private String responseDescription;
    @JsonProperty("requestId")
    private String requestId;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("transaction_date")
    private TransactionDate transactionDate;
    @JsonProperty("purchased_code")
    private String purchasedCode;
    @JsonProperty("mainToken")
    private String mainToken;
    @JsonProperty("mainTokenDescription")
    private String mainTokenDescription;
    @JsonProperty("mainTokenUnits")
    private Double mainTokenUnits;
    @JsonProperty("mainTokenTax")
    private Double mainTokenTax;
    @JsonProperty("mainsTokenAmount")
    private Double mainsTokenAmount;
    @JsonProperty("bonusToken")
    private String bonusToken;
    @JsonProperty("customerName")
    private String customerName;
    @JsonProperty("customerAddress")
    private String customerAddress;
    @JsonProperty("meterNumber")
    private String meterNumber;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Address")
    private String address;
    @JsonProperty("token")
    private String token;
    @JsonProperty("units")
    private String units;
    @JsonProperty("bonusTokenDescription")
    private String bonusTokenDescription;
    @JsonProperty("bonusTokenUnits")
    private Integer bonusTokenUnits;
    @JsonProperty("bonusTokenTax")
    private Double bonusTokenTax;
    @JsonProperty("bonusTokenAmount")
    private Double bonusTokenAmount;
    @JsonProperty("tariffIndex")
    private String tariffIndex;
    @JsonProperty("debtDescription")
    private String debtDescription;



}
