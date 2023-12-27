package com.stitch.vendor.vtpass;

import com.stitch.model.dtos.*;

public interface CableTvVendor {

    VTPassCablePackageDto getCableTvPackage(String serviceID);

    VTPassCableInfoResponse cableInfoEnquiry(String serviceID, String billerCode);

    VTPassCableTvPaymentResponseDto vendCablePlan(VTPassCableTvPaymentRequest vtPassCableTvPaymentRequest);

    VTPassMultichoiceDTO validateCableId(String  serviceID);
}
