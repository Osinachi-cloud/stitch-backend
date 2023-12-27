package com.stitch.vendor.vtpass;

import com.stitch.model.dtos.DataPlanDto;
import com.stitch.model.dtos.VTPassDataEmailVerificationResponse;
import com.stitch.model.dtos.VTPassDataPaymentResponseDto;
import com.stitch.model.dtos.VTPassDataRequest;

public interface DataVendor{

    VTPassDataPaymentResponseDto vendData(VTPassDataRequest airtimePayload);

    DataPlanDto getDataPackage(String network);

    VTPassDataEmailVerificationResponse verifySmileEmail(String serviceID, String emailAddress);
}
