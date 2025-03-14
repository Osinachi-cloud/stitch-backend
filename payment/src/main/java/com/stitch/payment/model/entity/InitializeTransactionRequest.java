package com.stitch.payment.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
//import model.MetaData;
//import model.PaystackBearer;

import javax.validation.ValidationException;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.List;

/**
 * This class represents the post object that would initialize a paystack transaction
 * to generate the url that would be used for payment.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class InitializeTransactionRequest {

    @Digits(integer = 9, fraction = 0)
    private BigDecimal amount;
    private String email;
    private String plan;
    private String reference;
    private String subaccount;
    private String callback_url;
    private BigDecimal quantity;
    private Integer invoice_limit;
    /**
     * Extra information to be saved with this transaction
     */
    private Integer transaction_charge;
    private List<String> channel;
    private String productId;
    private String productCategoryName;
    private String vendorId;
    private String narration;
    private String [] cartProductsIds;
    private String sleeveType;
    private String color;
    private String bodyMeasurementTag;
}
