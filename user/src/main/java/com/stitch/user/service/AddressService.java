package com.stitch.user.service;


import com.stitch.user.model.dto.AddressDto;
import com.stitch.user.model.entity.Address;

public interface AddressService {
    Address createAddress(AddressDto addressDto);

}
