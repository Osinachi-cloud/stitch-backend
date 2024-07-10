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
    /**
     * For recurring fees plan is set for non recurring fees plan is not set
     */
    private String plan;
    private String reference;

    /**
     *The code for the subaccount that owns the payment. e.g. ACCT_8f4s1eq7ml6rlzj
     */
    private String subaccount;

    /**
     * Who bears Paystack charges?
     */
//    private PaystackBearer bearer = PaystackBearer.ACCOUNT;


    /**
     * Fully qualified url, e.g. https://nut-ng.org/ .
     * Use this to override the callback url provided on the dashboard for this transaction
     */
    private String callback_url;

    /**
     * Used to apply a multiple to the amount returned by the plan code above.
     */
    private BigDecimal quantity;

    /**
     * Number of invoices to raise during the subscription.
     * Overrides invoice_limit set on plan.
     */
    private Integer invoice_limit;

    /**
     * Extra information to be saved with this transaction
     */
//    private MetaData metadata;

    /**
     * A flat fee to charge the subaccount for this transaction, in kobo.
     * This overrides the split percentage set when the subaccount was created.
     * Ideally, you will need to use this if you are splitting in flat rates
     * (since subaccount creation only allows for percentage split). e.g. 7000 for a 70 naira flat fee.
     */
    private Integer transaction_charge;

    private List<String> channel;

    private String productId;
    private String productCategoryName;
    private String vendorId;
    private String narration;
}
