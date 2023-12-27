package com.stitch.model.dtos;

import lombok.Data;

@Data
public class VTPasPostPaidDto {

    private String code;
    private Content content;
    private String responseDescription;
    private String requestId;
    private String amount;
    private TransactionDate transactionDate;
    private String purchasedCode;
    private String utilityName;
    private String exchangeReference;
    private String balance;

    public static class Content {
        private Transactions transactions;

        public static class Transactions {
            private int amount;
            private int convinienceFee;
            private String status;
            private Object name;
            private String phone;
            private String email;
            private String type;
            private String createdAt;
            private Object discount;
            private Object giftcardId;
            private int totalAmount;
            private int commission;
            private String channel;
            private String platform;
            private Object serviceVerification;
            private int quantity;
            private int unitPrice;
            private String uniqueElement;
            private String productName;

        }
    }

    public static class TransactionDate {
        private String date;
        private int timezoneType;
        private String timezone;

    }


}
