package com.stitch.service.impl;

import com.stitch.exception.BillingException;
import com.stitch.exception.StitchApiException;
import com.stitch.model.entity.BillTransaction;

import com.stitch.utils.RequestIdGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stitch.model.dtos.*;
import com.stitch.service.BillerService;
import com.stitch.service.BillerTransactionService;
import com.stitch.vendor.vtpass.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class BillerServiceImpl implements BillerService {

    private final AirtimeVendor airtimeVendor;
    private final DataVendor dataVendor;
    private final ElectricityVendor electricityVendor;
    private final CableTvVendor cableTvVendor;
    private final BillerTransactionService billerTransactionService;
    private final RequeryService requeryService;
    private final ObjectMapper mapper = new ObjectMapper();


    public BillerServiceImpl(AirtimeVendor airtimeVendor, DataVendor dataVendor, ElectricityVendor electricityVendor, CableTvVendor cableTvVendor, BillerTransactionService billerTransactionService, RequeryService requeryService) {

        this.airtimeVendor = airtimeVendor;
        this.dataVendor = dataVendor;
        this.electricityVendor = electricityVendor;
        this.cableTvVendor = cableTvVendor;
        this.billerTransactionService = billerTransactionService;
        this.requeryService = requeryService;
    }

    @Override
    public VTPassAirtimePaymentResponse vendAirtime(AirtimeRequest airtimeRequest) {

        log.debug("Vending airtime: {}", airtimeRequest);

        VTPassAirtimeRequest airtimePaymentRequest = VTPassAirtimeRequest.builder()
                .serviceId(airtimeRequest.getServiceID())
                .phone(airtimeRequest.getPhone())
                .amount(airtimeRequest.getAmount())
                .orderId(airtimeRequest.getOrderId())
                .customerId(airtimeRequest.getCustomerId())
                .build();

        final String requestId = RequestIdGenerator.generateRequestId();

        try {
            airtimePaymentRequest.setRequestId(requestId);

            VTPassAirtimePaymentResponse airtimePaymentResponse = this.airtimeVendor.vendAirTime(airtimePaymentRequest);
            airtimePaymentResponse.setRequestId(requestId);

            BillTransaction.BillTransactionBuilder billTransactionBuilder = BillTransaction.builder()
                    .orderId(airtimePaymentRequest.getOrderId())
                    .customerId(airtimePaymentRequest.getCustomerId())
                    .requestId(requestId)
                    .phoneNumber(airtimePaymentRequest.getPhone())
                    .billersCode(airtimePaymentRequest.getPhone())
                    .serviceId(airtimePaymentRequest.getServiceId())
                    .amount(new BigDecimal(airtimePaymentRequest.getAmount()))
                    .statusCode(airtimePaymentResponse.getCode())
                    .responseDescription(airtimePaymentResponse.getResponseDescription())
                    .vendorName("VTPASS")
                    .transactionType("AIRTIME")
                    .requestPayload(mapper.writeValueAsString(airtimePaymentRequest))
                    .responsePayload(mapper.writeValueAsString(airtimePaymentResponse));
            if (airtimePaymentResponse.getContent() != null && airtimePaymentResponse.getContent().getTransactions() != null) {
                VTPassAirtimePaymentResponse.Transactions transaction = airtimePaymentResponse.getContent().getTransactions();
                billTransactionBuilder.transactionStatus(transaction.getStatus())
                        .commission(transaction.getCommission())
                        .convenienceFee(transaction.getConvenienceFee())
                        .billerAmount(BigDecimal.valueOf(transaction.getTotalAmount()));
            }
            billerTransactionService.saveBillTransaction(billTransactionBuilder.build());
            return airtimePaymentResponse;

        } catch (StitchApiException e) {
            log.error(e.getMessage(), e);
            throw new BillingException(requestId, e.getMessage(), e);
        } catch (JsonProcessingException e) {
            log.error("Error writing object to JSON", e);
            throw new BillingException(requestId, "Error processing bill payment", e);
        }
    }

    @Override
    public VTPassDataPaymentResponseDto vendData(DataRequest dataRequest) {

        log.debug("Vending data: {}", dataRequest);

        VTPassDataRequest dataPaymentRequest = VTPassDataRequest.builder()
                .serviceID(dataRequest.getServiceID())
                .variation_code(dataRequest.getVariation_code())
                .phone(dataRequest.getPhone())
                .billersCode(dataRequest.getBillersCode())
                .amount(dataRequest.getAmount())
                .customerId(dataRequest.getCustomerId())
                .orderId(dataRequest.getOrderId())
                .build();

        final String requestId = RequestIdGenerator.generateRequestId();
        try {
            dataPaymentRequest.setRequest_id(requestId);

            VTPassDataPaymentResponseDto dataPaymentResponse = this.dataVendor.vendData(dataPaymentRequest);
            dataPaymentResponse.setRequestId(requestId);

            BillTransaction.BillTransactionBuilder billTransactionBuilder = BillTransaction.builder()
                    .orderId(dataPaymentRequest.getOrderId())
                    .customerId(dataPaymentRequest.getCustomerId())
                    .requestId(requestId)
                    .phoneNumber(dataPaymentRequest.getPhone())
                    .billersCode(dataPaymentRequest.getBillersCode())
                    .serviceId(dataPaymentRequest.getServiceID())
                    .amount(new BigDecimal(dataPaymentRequest.getAmount()))
                    .statusCode(dataPaymentResponse.getCode())
                    .responseDescription(dataPaymentResponse.getResponseDescription())
                    .vendorName("VTPASS")
                    .transactionType("DATA")
                    .requestPayload(mapper.writeValueAsString(dataPaymentRequest))
                    .responsePayload(mapper.writeValueAsString(dataPaymentResponse));
            if (dataPaymentResponse.getContent() != null && dataPaymentResponse.getContent().getTransactions() != null) {
                VTPassDataPaymentResponseDto.Transactions transaction = dataPaymentResponse.getContent().getTransactions();
                billTransactionBuilder.transactionStatus(transaction.getStatus())
                        .commission(transaction.getCommission())
                        .convenienceFee(transaction.getConvenienceFee())
                        .billerAmount(BigDecimal.valueOf(transaction.getTotalAmount()));
            }
            billerTransactionService.saveBillTransaction(billTransactionBuilder.build());
            return dataPaymentResponse;
        } catch (StitchApiException e) {
            log.error(e.getMessage(), e);
            throw new BillingException(requestId, e.getMessage(), e);
        } catch (JsonProcessingException e) {
            log.error("Error writing object to JSON", e);
            throw new BillingException(requestId, "Error processing bill payment", e);
        }
    }

    @Override
    public VTPassCableTvPaymentResponseDto vendCableTv(CableTvRequest cableRequest) {

        log.debug("Vending cable TV: {}", cableRequest);

        VTPassCableTvPaymentRequest cableTvPaymentRequest = VTPassCableTvPaymentRequest.builder()
                .customerId(cableRequest.getCustomerId())
                .orderId(cableRequest.getOrderId())
                .request_id(cableRequest.getRequest_id())
                .serviceID(cableRequest.getServiceID())
                .billersCode(cableRequest.getBillersCode())
                .variation_code(cableRequest.getVariation_code())
                .amount(cableRequest.getAmount())
                .phone(cableRequest.getPhone())
                .subscription_type(cableRequest.getSubscription_type())
                .quantity(cableRequest.getQuantity())
                .build();

        final String requestId = RequestIdGenerator.generateRequestId();
        try {

            cableTvPaymentRequest.setRequest_id(requestId);

            VTPassCableTvPaymentResponseDto cableTvPaymentResponse = this.cableTvVendor.vendCablePlan(cableTvPaymentRequest);
            cableTvPaymentResponse.setRequestId(requestId);

            BillTransaction.BillTransactionBuilder billTransactionBuilder = BillTransaction.builder()
                    .orderId(cableTvPaymentRequest.getOrderId())
                    .customerId(cableTvPaymentRequest.getCustomerId())
                    .phoneNumber(cableTvPaymentRequest.getPhone())
                    .requestId(requestId)
                    .billersCode(cableTvPaymentRequest.getBillersCode())
                    .variationCode(cableTvPaymentRequest.getVariation_code())
                    .amount(new BigDecimal(cableTvPaymentRequest.getAmount()))
                    .statusCode(cableTvPaymentResponse.getCode())
                    .vendorName("VTPASS")
                    .transactionType("CABLE-TV")
                    .responseDescription(cableTvPaymentResponse.getResponseDescription())
                    .requestPayload(mapper.writeValueAsString(cableTvPaymentRequest))
                    .responsePayload(mapper.writeValueAsString(cableTvPaymentResponse));


            if (cableTvPaymentResponse.getContent() != null && cableTvPaymentResponse.getContent().getTransactions() != null) {
                VTPassCableTvPaymentResponseDto.Transactions transaction = cableTvPaymentResponse.getContent().getTransactions();
                billTransactionBuilder.transactionStatus(transaction.getStatus())
                        .commission(transaction.getCommission())
                        .convenienceFee(transaction.getConvenienceFee())
                        .billerAmount(BigDecimal.valueOf(transaction.getTotalAmount()));
            }
            billerTransactionService.saveBillTransaction(billTransactionBuilder.build());
            return cableTvPaymentResponse;
        } catch (StitchApiException e) {
            log.error(e.getMessage(), e);
            throw new BillingException(requestId, e.getMessage(), e);
        } catch (JsonProcessingException e) {
            log.error("Error writing object to JSON", e);
            throw new BillingException(requestId, "Error processing bill payment", e);
        }
    }

    @Override
    public ElectricityPaymentResponse vendElectricity(ElectricityRequest electricityRequest) {

        log.debug("Vending electricity: {}", electricityRequest);

        VTPassElectricityPaymentRequest electricityPaymentRequest = VTPassElectricityPaymentRequest.builder()
                .amount(electricityRequest.getAmount())
                .billersCode(electricityRequest.getBillersCode())
                .customerId(electricityRequest.getCustomerId())
                .phone(electricityRequest.getPhone())
                .serviceID(electricityRequest.getServiceID())
                .orderId(electricityRequest.getOrderId())
                .variation_code(electricityRequest.getVariation_code())
                .build();

        final String requestId = RequestIdGenerator.generateRequestId();
        final String variationCode = electricityRequest.getVariation_code();
        try {
            electricityPaymentRequest.setRequest_id(requestId);

            VTPassElectricityResponseDto vtPassElectricityResponseDto;

            if ("prepaid".equalsIgnoreCase(variationCode)) {
                vtPassElectricityResponseDto = this.electricityVendor.vendPrepaidElectricity(electricityPaymentRequest);
            } else if ("postpaid".equalsIgnoreCase(variationCode)) {
                vtPassElectricityResponseDto = this.electricityVendor.vendPostpaidElectricity(electricityPaymentRequest);
            } else {
                throw new BillingException("Unknown electricity variation code: " + variationCode);
            }
            vtPassElectricityResponseDto.setRequestId(requestId);

            BillTransaction.BillTransactionBuilder billTransactionBuilder = BillTransaction.builder()
                    .customerId(electricityPaymentRequest.getCustomerId())
                    .orderId(electricityPaymentRequest.getOrderId())
                    .requestId(requestId)
                    .billersCode(electricityPaymentRequest.getBillersCode())
                    .phoneNumber(electricityPaymentRequest.getPhone())
                    .serviceId(electricityPaymentRequest.getServiceID())
                    .variationCode(electricityPaymentRequest.getVariation_code())
                    .billersCode(electricityPaymentRequest.getBillersCode())
                    .customerName(vtPassElectricityResponseDto.getCustomerName() != null ? vtPassElectricityResponseDto.getCustomerName() : vtPassElectricityResponseDto.getName())
                    .customerAddress(vtPassElectricityResponseDto.getCustomerAddress() != null ? vtPassElectricityResponseDto.getCustomerAddress() : vtPassElectricityResponseDto.getAddress())
                    .token(vtPassElectricityResponseDto.getPurchasedCode() != null ? vtPassElectricityResponseDto.getPurchasedCode().replaceAll("(?:Token : |-)", "").trim() : null)
                    .units(vtPassElectricityResponseDto.getUnits())
                    .amount(new BigDecimal(vtPassElectricityResponseDto.getAmount()))
                    .statusCode(vtPassElectricityResponseDto.getCode())
                    .responseDescription(vtPassElectricityResponseDto.getResponseDescription())
                    .vendorName("VTPASS")
                    .transactionType("ELECTRICITY")
                    .requestPayload(mapper.writeValueAsString(electricityPaymentRequest))
                    .responsePayload(mapper.writeValueAsString(vtPassElectricityResponseDto));

            if (vtPassElectricityResponseDto.getContent() != null && vtPassElectricityResponseDto.getContent().getTransactions() != null) {
                VTPassElectricityResponseDto.Transactions transaction = vtPassElectricityResponseDto.getContent().getTransactions();
                billTransactionBuilder.transactionStatus(transaction.getStatus())
                        .commission(Double.valueOf(transaction.getCommission()))
                        .convenienceFee(Double.valueOf(transaction.getConvenienceFee()))
                        .billerAmount(BigDecimal.valueOf(transaction.getTotalAmount()));
            }
            billerTransactionService.saveBillTransaction(billTransactionBuilder.build());

            return ElectricityPaymentResponse.builder()
                    .code(vtPassElectricityResponseDto.getCode())
                    .description(vtPassElectricityResponseDto.getResponseDescription())
                    .productName(vtPassElectricityResponseDto.getContent().getTransactions().getProductName())
                    .token(vtPassElectricityResponseDto.getPurchasedCode() != null ? vtPassElectricityResponseDto.getPurchasedCode().replaceAll("(?:Token : |-)", "").trim() : null)
                    .units(vtPassElectricityResponseDto.getUnits())
                    .build();
        } catch (StitchApiException e) {
            log.error(e.getMessage(), e);
            throw new BillingException(requestId, e.getMessage(), e);
        } catch (JsonProcessingException e) {
            log.error("Error writing object to JSON", e);
            throw new BillingException(requestId, "Error processing bill payment", e);
        }
    }


    @Override
    public VTPassRequeryResponse requeryTransaction(BillPaymentRequest paymentRequest) {

        log.debug("Doing requery of transaction: {}", paymentRequest);
        try {

            VTPassRequeryRequest VTPassRequeryRequest = new VTPassRequeryRequest(paymentRequest.getRequestId());

            VTPassRequeryResponse requeryResponse = this.requeryService.query(VTPassRequeryRequest);

            BillTransaction billTransaction = billerTransactionService.retrieveBillTransactionByRequestId(paymentRequest.getRequestId());

            if (billTransaction == null) {

                billTransaction = BillTransaction.builder()
                        .orderId(paymentRequest.getOrderId())
                        .customerId(paymentRequest.getCustomerId())
                        .requestId(paymentRequest.getRequestId())
                        .serviceId(paymentRequest.getServiceId())
                        .vendorName("VTPASS")
                        .transactionType(paymentRequest.getCategory())
                        .requestPayload(mapper.writeValueAsString(paymentRequest))
                        .build();
            }
            billTransaction.setAmount(new BigDecimal(requeryResponse.getAmount()));
            billTransaction.setStatusCode(requeryResponse.getCode());
            billTransaction.setResponseDescription(requeryResponse.getResponseDescription());
            billTransaction.setResponsePayload(mapper.writeValueAsString(requeryResponse));

            if (requeryResponse.getContent() != null && requeryResponse.getContent().getTransactions() != null) {
                VTPassRequeryResponse.Transactions transaction = requeryResponse.getContent().getTransactions();
                billTransaction.setTransactionStatus(transaction.getStatus());
                billTransaction.setCommission(transaction.getCommission());
                billTransaction.setConvenienceFee(transaction.getConvenienceFee());
                billTransaction.setBillerAmount(BigDecimal.valueOf(transaction.getTotalAmount()));
            }
            billerTransactionService.saveBillTransaction(billTransaction);
            return requeryResponse;
        } catch (StitchApiException e) {
            log.error(e.getMessage(), e);
            throw new BillingException(e.getMessage(), e);
        } catch (JsonProcessingException e) {
            log.error("Error writing object to JSON", e);
            throw new BillingException("Error doing VTPass requery of transaction", e);
        }
    }

    @Override
    public VTPassRequeryResponse requeryTransaction(String orderId) {

        log.debug("Doing requery of transaction for orderId [{}]", orderId);

        BillTransaction billTransaction = billerTransactionService.retrieveBillTransactionByOrderId(orderId);

        try {

            VTPassRequeryRequest VTPassRequeryRequest = new VTPassRequeryRequest(billTransaction.getRequestId());
            VTPassRequeryResponse requeryResponse = this.requeryService.query(VTPassRequeryRequest);

            billTransaction.setAmount(new BigDecimal(requeryResponse.getAmount()));
            billTransaction.setStatusCode(requeryResponse.getCode());
            billTransaction.setResponseDescription(requeryResponse.getResponseDescription());
            billTransaction.setResponsePayload(mapper.writeValueAsString(requeryResponse));

            if (requeryResponse.getContent() != null && requeryResponse.getContent().getTransactions() != null) {
                VTPassRequeryResponse.Transactions transaction = requeryResponse.getContent().getTransactions();
                billTransaction.setTransactionStatus(transaction.getStatus());
                billTransaction.setCommission(transaction.getCommission());
                billTransaction.setConvenienceFee(transaction.getConvenienceFee());
                billTransaction.setBillerAmount(BigDecimal.valueOf(transaction.getTotalAmount()));
            }
            billerTransactionService.saveBillTransaction(billTransaction);
            return requeryResponse;
        } catch (StitchApiException e) {
            log.error(e.getMessage(), e);
            throw new BillingException(e.getMessage(), e);
        } catch (JsonProcessingException e) {
            log.error("Error writing object to JSON", e);
            throw new BillingException("Error doing VTPass requery of transaction", e);
        }
    }


    @Override
    public DataPlanDto getDataPackages(String serviceId) {
        return dataVendor.getDataPackage(serviceId);
    }

    @Override
    public VTPassCablePackageDto getCableTvPackages(String serviceId) {
        return cableTvVendor.getCableTvPackage(serviceId);
    }

    @Override
    public VTPassCableInfoResponse cableInfoEnquiry(String serviceId, String smartCard) {
        return cableTvVendor.cableInfoEnquiry(serviceId, smartCard);
    }

    @Override
    public VTPassDataEmailVerificationResponse verifyInternetUserEmail(String serviceId, String emailAddress) {
        return dataVendor.verifySmileEmail(serviceId, emailAddress);
    }

    @Override
    public MeterValidationResponse validateMeterNumber(ElectricityMeterValidationRequest request) {
        VTPassElectricityMeterValidation meterValidation = VTPassElectricityMeterValidation.builder()
                .serviceID(request.getServiceID())
                .billersCode(request.getNumber())
                .type(request.getType())
                .build();
        return electricityVendor.validateMeterNumber(meterValidation);
    }

}
