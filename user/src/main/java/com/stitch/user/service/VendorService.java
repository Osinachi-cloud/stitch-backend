package com.stitch.user.service;

import com.stitch.commons.model.dto.Response;
import com.stitch.user.model.dto.*;

public interface VendorService {

    VendorDto createVendor(VendorRequest vendorRequest);

    VendorDto updateVendor(VendorUpdateRequest vendorRequest, String emailAddress);

    Response updateVendorProfileImage(String profileImage, String emailAddress);

    VendorDto getVendor(String vendorId);

    VendorDto getVendorByEmail(String emailAddress);
}
