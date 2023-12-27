package com.stitch.payment.repository.projection;

import com.stitch.payment.model.enums.TransactionStatus;
import com.stitch.payment.model.enums.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;

public interface TransactionView {

     Long getId();
     Instant getDateCreated();
     String getTransactionId();
     BigDecimal getAmount();
     String getCurrency();
     BigDecimal getSrcAmount();
     String getSrcCurrency();
     String getPaymentMode();
     String getProductCategory();
     String getNarration();
     String getDescription();
     TransactionStatus getStatus();
     TransactionType getTransactionType();
     String getProductPackage();
     String getProductProvider();
     String getPhoneNumber();
     String getVariationCode();
     String getBillersCode();
     String getCustomerName();
     String getCustomerAddress();

     String getToken();

     String getUnits();
    String getOrderId();
    String getFirstName();
    String getLastName();

}
