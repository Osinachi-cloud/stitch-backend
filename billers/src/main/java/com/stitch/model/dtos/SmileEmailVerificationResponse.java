package com.stitch.model.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SmileEmailVerificationResponse {

    @JsonProperty("code")
    public String code;
    @JsonProperty("content")
    public Content content;


    @Data
    public static class Content {

        @JsonProperty("Customer_Name")
        public String customerName;
        @JsonProperty("AccountList")
        public AccountList accountList;
    }

    @Data
    public static class AccountList {

        @JsonProperty("Account")
        public List<Account> accounts;
        @JsonProperty("NumberOfAccounts")
        public Integer numberOfAccounts;
    }

    @Data
    public static class Account {

        @JsonProperty("AccountId")
        public Long accountId;
        @JsonProperty("FriendlyName")
        public String friendlyName;

    }

}