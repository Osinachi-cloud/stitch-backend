package com.stitch.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VTPassDataEmailVerificationResponse {

    String customerName;
    List<Account> accounts = new ArrayList<>();

    @Data
    public static class Account{
        String accountId;
        String accountName;
    }

}
