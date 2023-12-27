package com.stitch.psp.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stitch.currency.model.enums.Currency;
import com.stitch.psp.exception.PaymentServiceException;
import com.stitch.psp.model.PaymentVerificationRequest;
import com.stitch.psp.model.PaymentVerificationResponse;
import com.stitch.psp.model.VerificationStatus;
import com.stitch.psp.model.dto.flutterwave.CardPayment;
import com.stitch.psp.model.dto.flutterwave.FlutterwaveVerificationResponse;
import com.stitch.psp.model.entity.PaymentVerification;
import com.stitch.psp.model.enums.PaymentProvider;
import com.stitch.psp.repository.PaymentVerificationRepository;
import com.stitch.psp.service.LocalPaymentProviderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service("local")
public class LocalPaymentProviderServiceImpl implements LocalPaymentProviderService {


    private final FlutterwaveService flutterwaveService;
    private final PaymentVerificationRepository paymentVerificationRepository;
    private final ObjectMapper mapper;

    @Value("${psp.local-default}")
    private String localPaymentProvider;

    public LocalPaymentProviderServiceImpl(FlutterwaveService flutterwaveService, PaymentVerificationRepository paymentVerificationRepository) {
        this.flutterwaveService = flutterwaveService;
        this.paymentVerificationRepository = paymentVerificationRepository;
        this.mapper = new ObjectMapper();
    }


    @Override
    public PaymentVerificationResponse verifyTransaction(PaymentVerificationRequest paymentVerificationRequest) {

        log.debug("Verifying transaction [{}] using local payment provider [{}]", paymentVerificationRequest, localPaymentProvider);

        PaymentVerificationResponse paymentVerificationResponse = new PaymentVerificationResponse();
        paymentVerificationResponse.setTransactionRefId(paymentVerificationRequest.getTransactionRefId());
        paymentVerificationResponse.setCurrency(Currency.valueOf(paymentVerificationRequest.getCurrency()));
        paymentVerificationResponse.setStatus(VerificationStatus.PENDING);


        if ("flutterwave".equalsIgnoreCase(localPaymentProvider)) {

            paymentVerificationResponse.setPaymentProvider(PaymentProvider.FLUTTERWAVE);
            try {
                FlutterwaveVerificationResponse response = (FlutterwaveVerificationResponse) flutterwaveService.verifyPayment(paymentVerificationRequest.getTransactionRefId());

                PaymentVerification paymentVerification = PaymentVerification.builder()
                        .amount(paymentVerificationRequest.getAmount())
                        .currency(paymentVerificationResponse.getCurrency())
                        .provider(PaymentProvider.FLUTTERWAVE)
                        .transactionId(paymentVerificationRequest.getTransactionRefId())
                        .status(response.getStatus())
                        .build();
                savePaymentVerificationResponse(paymentVerification, mapper.writeValueAsString(paymentVerificationRequest), mapper.writeValueAsString(response));


                if ((("success".equalsIgnoreCase(response.getStatus())) || ("successful".equalsIgnoreCase(response.getStatus()))) &&
                        response.getData().getAmount() >= paymentVerificationRequest.getAmount().doubleValue()
                        && response.getData().getCurrency().equalsIgnoreCase(paymentVerificationRequest.getCurrency())
                ) {
                    log.debug("Transaction is successful");
                    paymentVerificationResponse.setTransactionRefId(paymentVerificationRequest.getTransactionRefId());
                    paymentVerificationResponse.setStatus(VerificationStatus.SUCCESSFUL);
                    paymentVerificationResponse.setLast4digits(response.getData().getCard().getLast4digits());
                    paymentVerificationResponse.setFirst6digits(response.getData().getCard().getFirst6digits());
                    paymentVerificationResponse.setExpiry(response.getData().getCard().getExpiry());
                    paymentVerificationResponse.setCountry(response.getData().getCard().getCountry());
                    paymentVerificationResponse.setType(response.getData().getCard().getType());
                    paymentVerificationResponse.setToken(response.getData().getCard().getToken());

                } else {
                    log.debug("Transaction failed");
                    paymentVerificationResponse.setStatus(VerificationStatus.FAILED);
                }
            } catch (Exception e) {
                log.error("Error calling Flutterwave API", e);
                throw new PaymentServiceException("Cannot verify transaction with ID: " + paymentVerificationRequest.getTransactionRefId(), e);
            }
        } else {
            // Use the alternative local payment provider configured in the application properties
            // Otherwise throw an exception
            throw new PaymentServiceException("No alternative local payment provider configured");
        }
        return paymentVerificationResponse;
    }


    @Override
    public PaymentVerificationResponse chargeCard(CardPayment cardPayment) {

        log.debug("Card transaction [{}] using local default payment provider [{}]", cardPayment, localPaymentProvider);

        PaymentVerificationResponse paymentVerificationResponse = new PaymentVerificationResponse();
        paymentVerificationResponse.setStatus(VerificationStatus.PENDING);

        if ("flutterwave".equalsIgnoreCase(localPaymentProvider)) {

            try {
                FlutterwaveVerificationResponse response = flutterwaveService.makeCardPayment(cardPayment);

                paymentVerificationResponse.setTransactionRefId(response.getData().getTxRef());

                PaymentVerification paymentVerification = PaymentVerification.builder()
                        .amount(cardPayment.getAmount())
                        .currency(cardPayment.getDestCurrency())
                        .provider(PaymentProvider.FLUTTERWAVE)
                        .transactionId(cardPayment.getTransactionReference())
                        .status(response.getStatus())
                        .build();
                savePaymentVerificationResponse(paymentVerification, mapper.writeValueAsString(cardPayment), mapper.writeValueAsString(response));

                if ("success".equalsIgnoreCase(response.getStatus()) || "successful".equalsIgnoreCase(response.getStatus())) {
                    log.debug("Card Payment Transaction is successful");
                    paymentVerificationResponse.setStatus(VerificationStatus.SUCCESSFUL);
                } else {
                    log.debug("Card Payment Transaction failed");
                    paymentVerificationResponse.setStatus(VerificationStatus.FAILED);
                }
            } catch (Exception e) {
                log.error("Error calling Flutterwave API for card payment", e);
                throw new PaymentServiceException("Failed to make payment via card.", e);
            }
        }
        else {
            // Use the alternative local payment provider configured in the application properties
            // Otherwise throw an exception
            throw new PaymentServiceException("No alternative local payment provider configured");
        }
        return paymentVerificationResponse;
    }


    private void savePaymentVerificationResponse(PaymentVerification paymentVerification, String request, String response) throws JsonProcessingException {
        paymentVerification.setRequest(request);
        paymentVerification.setResponse(response);
        paymentVerificationRepository.saveAndFlush(paymentVerification);
    }
}
