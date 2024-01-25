package com.stitch.user.service.impl;

import com.stitch.commons.model.dto.Response;
import com.stitch.user.model.dto.VendorDto;
import com.stitch.user.model.dto.VendorRequest;
import com.stitch.user.model.dto.VendorUpdateRequest;
import com.stitch.user.service.VendorService;

public class VendorServiceImpl implements VendorService {
    @Override
    public VendorDto createVendor(VendorRequest vendorRequest) {
        return null;
    }

    @Override
    public VendorDto updateVendor(VendorUpdateRequest vendorRequest, String emailAddress) {
        return null;
    }

    @Override
    public Response updateVendorProfileImage(String profileImage, String emailAddress) {
        return null;
    }

    @Override
    public VendorDto getVendor(String vendorId) {
        return null;
    }

    @Override
    public VendorDto getVendorByEmail(String emailAddress) {
        return null;
    }
}
