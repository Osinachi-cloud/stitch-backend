package com.stitch.gateway.model;

import com.stitch.user.model.dto.CustomerDto;
import com.stitch.wallet.model.dto.WalletDto;
import com.stitch.gateway.security.model.Token;
import lombok.Data;

import java.util.List;

@Data
public class LoginResponse {

    private String customerId;
    private String tier;
    private String country;
    private String password;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;
    private boolean hasPin;

    private boolean saveCard;
    private boolean enablePush;
    private String accessToken;
    private String refreshToken;
    private String profileImage;
    private List<WalletDto> wallets;

    public LoginResponse(CustomerDto customer, Token token) {
        this.customerId = customer.getCustomerId();
        this.tier = customer.getTier();
        this.country = customer.getCountry();
        this.firstName = customer.getFirstName();
        this.lastName = customer.getLastName();
        this.emailAddress = customer.getEmailAddress();
        this.phoneNumber = customer.getPhoneNumber();
        this.hasPin = customer.isHasPin();
        this.saveCard = customer.isSaveCard();
        this.enablePush = customer.isEnablePush();
        this.accessToken = token.getAccessToken();
        this.refreshToken = token.getRefreshToken();
        this.profileImage = customer.getProfileImage();
    }
}
