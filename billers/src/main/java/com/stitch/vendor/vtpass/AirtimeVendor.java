package com.stitch.vendor.vtpass;

import com.stitch.model.dtos.VTPassAirtimeRequest;
import com.stitch.model.dtos.VTPassAirtimePaymentResponse;

public interface AirtimeVendor {

    VTPassAirtimePaymentResponse vendAirTime(VTPassAirtimeRequest airtimePayload);

}
