package com.stitch.psp.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stitch.currency.model.enums.Currency;
import com.stitch.currency.service.ExchangeRateService;
import com.stitch.psp.exception.PaymentServiceException;
import com.stitch.psp.model.PaymentVerificationRequest;
import com.stitch.psp.model.PaymentVerificationResponse;
import com.stitch.psp.model.VerificationStatus;
import com.stitch.psp.model.dto.PaymentIntentRequest;
import com.stitch.psp.model.dto.PaymentIntentResponse;
import com.stitch.psp.model.dto.flutterwave.CardPayment;
import com.stitch.psp.model.dto.stripe.StripeVerificationResponse;
import com.stitch.psp.model.entity.PaymentVerification;
import com.stitch.psp.model.enums.PaymentProvider;
import com.stitch.psp.repository.PaymentVerificationRepository;
import com.stitch.psp.service.ForeignPaymentProviderService;
import com.stripe.model.PaymentIntent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service("foreign")
public class ForeignPaymentProviderServiceImpl implements ForeignPaymentProviderService {

    private final StripeService stripeService;
    private final PaymentVerificationRepository paymentVerificationRepository;
    private final ExchangeRateService exchangeRateService;
    private final ObjectMapper mapper;
    @Value("${psp.foreign-default}")
    private String foreignPaymentProvider;

    public ForeignPaymentProviderServiceImpl(StripeService stripeService, PaymentVerificationRepository paymentVerificationRepository, ExchangeRateService exchangeRateService) {
        this.stripeService = stripeService;
        this.paymentVerificationRepository = paymentVerificationRepository;
        this.exchangeRateService = exchangeRateService;
        this.mapper = new ObjectMapper();

    }


    @Override
    public PaymentIntentResponse createPaymentIntent(PaymentIntentRequest paymentIntentRequest) {

        if ("stripe".equalsIgnoreCase(foreignPaymentProvider)) {
            return stripeService.createPaymentIntent(paymentIntentRequest);
        } else {
            // Use the alternative foreign payment provider configured in the application properties
            // Otherwise throw an exception
            throw new PaymentServiceException("No alternative foreign payment provider configured");
        }
    }

    @Override
    public PaymentVerificationResponse verifyTransaction(PaymentVerificationRequest paymentVerificationRequest) {

        log.debug("Verifying transaction [{}] using foreign payment provider [{}]", paymentVerificationRequest, foreignPaymentProvider);

        PaymentVerificationResponse paymentVerificationResponse = new PaymentVerificationResponse();
        paymentVerificationResponse.setTransactionRefId(paymentVerificationRequest.getTransactionRefId());
        paymentVerificationResponse.setCurrency(Currency.valueOf(paymentVerificationRequest.getCurrency()));
        paymentVerificationResponse.setStatus(VerificationStatus.PENDING);


        if ("stripe".equalsIgnoreCase(foreignPaymentProvider)) {

            paymentVerificationResponse.setPaymentProvider(PaymentProvider.STRIPE);

            try {
                StripeVerificationResponse stripeVerificationResponse = stripeService.verifyPayment(paymentVerificationRequest.getTransactionRefId());

                PaymentVerification paymentVerification = PaymentVerification.builder()
                        .amount(paymentVerificationRequest.getAmount())
                        .currency(paymentVerificationResponse.getCurrency())
                        .provider(PaymentProvider.STRIPE)
                        .transactionId(paymentVerificationRequest.getTransactionRefId())
                        .status(stripeVerificationResponse.getStatus())
                        .build();
                savePaymentVerificationResponse(paymentVerification, mapper.writeValueAsString(paymentVerificationRequest), stripeVerificationResponse.getRawResponse());

                if ((("succeeded".equalsIgnoreCase(stripeVerificationResponse.getStatus()))) &&
                        stripeVerificationResponse.getAmount() >= ((long)paymentVerificationRequest.getAmount().doubleValue() * 100)
                        && stripeVerificationResponse.getCurrency().equalsIgnoreCase(paymentVerificationRequest.getCurrency())
                ) {
                    log.debug("Transaction is successful: {}", stripeVerificationResponse);
                    paymentVerificationResponse.setTransactionRefId(paymentVerificationRequest.getTransactionRefId());
                    paymentVerificationResponse.setStatus(VerificationStatus.SUCCESSFUL);
                    paymentVerificationResponse.setLast4digits(stripeVerificationResponse.getLast4digits());
                    paymentVerificationResponse.setCountry(stripeVerificationResponse.getCountry());
                    paymentVerificationResponse.setType(stripeVerificationResponse.getCardType());
                    paymentVerificationResponse.setToken(stripeVerificationResponse.getPaymentMethodId());
                    paymentVerificationResponse.setCardFingerprint(stripeVerificationResponse.getCardFingerprint());
                    if (stripeVerificationResponse.getCardExpMonth() != null && stripeVerificationResponse.getCardExpYear() != null) {
                        paymentVerificationResponse.setExpiry(stripeVerificationResponse.getCardExpMonth() + "/" + stripeVerificationResponse.getCardExpYear());
                    }
                } else {
                    log.debug("Transaction failed: {}", stripeVerificationResponse);
                    paymentVerificationResponse.setStatus(VerificationStatus.FAILED);
                }
            } catch (Exception e) {
                log.error("Error verifying transaction: {}", paymentVerificationRequest, e);
                throw new PaymentServiceException(e);
            }
        } else {
            // Use the alternative foreign payment provider configured in the application properties
            // Otherwise throw an exception
            throw new PaymentServiceException("No alternative foreign payment provider configured");
        }
        return paymentVerificationResponse;
    }

    @Override
    public PaymentVerificationResponse chargeCard(CardPayment cardPayment) {
        if (cardPayment.getSrcCurrency().equals(Currency.NGN)) {
            BigDecimal chargeAmount = exchangeRateService.getEquivalentCurrencyAmount(cardPayment.getDestCurrency(), cardPayment.getAmount());
            cardPayment.setAmount(chargeAmount);
        }

        PaymentVerificationResponse paymentVerificationResponse = new PaymentVerificationResponse();
        paymentVerificationResponse.setStatus(VerificationStatus.PENDING);

        if ("stripe".equalsIgnoreCase(foreignPaymentProvider)) {

            try {
                PaymentIntentRequest paymentRequest = PaymentIntentRequest.builder()
                        .paymentMethodId(cardPayment.getToken())
                        .emailAddress(cardPayment.getEmail())
                        .customerId(cardPayment.getCustomerId())
                        .amount(cardPayment.getAmount())
                        .currency(cardPayment.getDestCurrency())
                        .build();

                PaymentIntent paymentResponse = stripeService.makeCardPayment(paymentRequest);

                PaymentVerification paymentVerification = PaymentVerification.builder()
                        .amount(cardPayment.getAmount())
                        .currency(cardPayment.getDestCurrency())
                        .provider(PaymentProvider.STRIPE)
                        .transactionId(paymentResponse.getId())
                        .status(paymentResponse.getStatus())
                        .build();
                savePaymentVerificationResponse(paymentVerification, mapper.writeValueAsString(cardPayment), paymentResponse.toJson());


                if ("succeeded".equalsIgnoreCase(paymentResponse.getStatus())) {
                    paymentVerificationResponse.setStatus(VerificationStatus.SUCCESSFUL);
                } else {
                    paymentVerificationResponse.setStatus(VerificationStatus.FAILED);
                }
            } catch (PaymentServiceException exception) {
                log.error(exception.getMessage());
                throw exception;
            }

            catch (Exception e) {
                log.error("Error calling Stripe API for card payment", e);
                throw new PaymentServiceException("Failed to make card payment", e);
            }
        }

        else {
            // Use the alternative foreign payment provider configured in the application properties
            // Otherwise throw an exception
            throw new PaymentServiceException("No alternative foreign payment provider configured");
        }
        return paymentVerificationResponse;
    }

    private void savePaymentVerificationResponse(PaymentVerification paymentVerification, String request, String response) throws JsonProcessingException {
        paymentVerification.setRequest(request);
        paymentVerification.setResponse(response);
        paymentVerificationRepository.saveAndFlush(paymentVerification);
    }
}
