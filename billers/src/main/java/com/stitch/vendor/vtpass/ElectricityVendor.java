package com.stitch.vendor.vtpass;

import com.stitch.model.dtos.*;

public interface ElectricityVendor {

    VTPassElectricityPrePaidResponseDto vendPrepaidElectricity(VTPassElectricityPaymentRequest airtimePayload);
    VTPassElectricityPostPaidResponseDto vendPostpaidElectricity(VTPassElectricityPaymentRequest airtimePayload);

    MeterValidationResponse validateMeterNumber(VTPassElectricityMeterValidation meterNumber);

}
