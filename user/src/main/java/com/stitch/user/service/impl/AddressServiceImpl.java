package com.stitch.user.service.impl;

import com.stitch.user.model.dto.AddressDto;
import com.stitch.user.model.entity.Address;
import com.stitch.user.repository.AddressRepository;
import com.stitch.user.service.AddressService;
import org.springframework.stereotype.Service;

import static com.stitch.user.util.DtoMapper.mapAddressDtoToAddress;
import static com.stitch.user.util.DtoMapper.mapAddressToDto;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Address createAddress(AddressDto addressDto){
        return addressRepository.save(mapAddressDtoToAddress(addressDto));
    }
}
