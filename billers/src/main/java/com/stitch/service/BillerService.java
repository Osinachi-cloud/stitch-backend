package com.stitch.service;

import com.stitch.model.dtos.*;


public interface BillerService {

    VTPassAirtimePaymentResponse vendAirtime(AirtimeRequest airtimeRequest);

    VTPassDataPaymentResponseDto vendData(DataRequest dataRequest);

    VTPassCableTvPaymentResponseDto vendCableTv(CableTvRequest cableRequest);

    ElectricityPaymentResponse vendElectricity(ElectricityRequest electricityRequest);

    VTPassRequeryResponse requeryTransaction(String orderId);

    DataPlanDto getDataPackages(String serviceId);

    VTPassCablePackageDto getCableTvPackages(String serviceId);

    VTPassCableInfoResponse cableInfoEnquiry(String serviceId, String smartCard);

    VTPassDataEmailVerificationResponse verifyInternetUserEmail(String serviceId, String emailAddress);

    MeterValidationResponse validateMeterNumber(ElectricityMeterValidationRequest request);

    VTPassRequeryResponse requeryTransaction(BillPaymentRequest paymentRequest);
}
