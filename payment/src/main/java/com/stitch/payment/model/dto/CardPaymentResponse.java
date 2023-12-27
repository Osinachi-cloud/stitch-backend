package com.stitch.payment.model.dto;

import com.stitch.payment.model.enums.CardType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CardPaymentResponse {
    private String cardId;
    private String last4digits;
    private String first6digits;
    private String expiry;
    private CardType cardType;

}
