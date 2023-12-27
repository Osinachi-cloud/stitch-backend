package com.stitch.user.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {

    private String country;
    private String state;
    private String city;
    private String street;
    private String houseNumber;
    private String apartmentNumber;
    private String postalCode;
    private String fullAddress;
}
