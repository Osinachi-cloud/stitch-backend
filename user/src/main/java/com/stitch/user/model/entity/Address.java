package com.stitch.user.model.entity;


import com.stitch.commons.model.entity.BaseEntity;
import lombok.Data;

import jakarta.persistence.*;

@Data
@Entity
@Table(name = "address")
public class Address extends BaseEntity {

    @Column(name = "country")
    private String country;

    @Column(name = "state")
    private String state;

    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String street;

    @Column(name = "house_number")
    private String houseNumber;

    @Column(name = "apartment_number")
    private String apartmentNumber;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "full_address")
    private String fullAddress;
}
