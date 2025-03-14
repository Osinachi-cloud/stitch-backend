package com.stitch.user.model.dto;


import com.stitch.user.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static com.stitch.user.util.DtoMapper.mapRoleToDto;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto implements Serializable {

    private static final long serialVersionUID = 2345L;

    private String userId;
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
    private RoleDto role;
    private String roleName;
//    private RoleDto adminRole;
    private String profileImage;

    public CustomerDto(UserEntity customer){
        this.userId = customer.getUserId();
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
        this.role = mapRoleToDto(customer.getRole());
        this.roleName = customer.getRole().getName();
    }


}
