package com.stitch.payment.model.dto;


import com.stitch.currency.model.enums.Currency;
import com.stitch.payment.model.enums.PaymentMode;
import com.stitch.payment.model.enums.TransactionStatus;
import com.stitch.payment.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionRequest {

    private String orderId;
    private String customerId;
    private String walletId;
    private String cardId;
    private String cardTransactionId;
    private String walletTransactionId;
    private BigDecimal amount;
    private Currency currency;
    private PaymentMode paymentMode;
    private BigDecimal srcAmount;
    private Currency srcCurrency;
    private BigDecimal destAmount;
    private Currency destCurrency;
    private BigDecimal exchangeRate;
    private BigDecimal fee;
    private String productCategory;
    private String narration;
    private String description;
    private TransactionType transactionType;
    private TransactionStatus status;
}
