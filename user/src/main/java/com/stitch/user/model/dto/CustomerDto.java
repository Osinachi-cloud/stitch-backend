package com.stitch.user.model.dto;


import com.stitch.user.model.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto implements Serializable {

    private static final long serialVersionUID = 2345L;

    private String customerId;
    private String tier;
    private String country;
    private String password;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;
    private boolean enabled;
    private boolean accountLocked;
    private boolean hasPin;

    private boolean saveCard;

    private boolean enablePush;
    private String profileImage;

    public CustomerDto(Customer customer){
        this.customerId = customer.getCustomerId();
        this.firstName = customer.getFirstName();
        this.lastName = customer.getLastName();
        this.emailAddress = customer.getEmailAddress();
        this.phoneNumber = customer.getPhoneNumber();
        this.tier = customer.getTier().name();
        this.country = customer.getCountry();
        this.hasPin = customer.getPin() != null;
        this.saveCard = customer.isSaveCard();
        this.enablePush = customer.isEnablePush();
        this.profileImage = customer.getProfileImage();
    }


}
