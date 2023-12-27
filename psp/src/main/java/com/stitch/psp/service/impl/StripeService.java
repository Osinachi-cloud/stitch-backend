package com.stitch.psp.service.impl;

import com.stitch.psp.exception.PaymentServiceException;
import com.stitch.psp.model.dto.PaymentIntentRequest;
import com.stitch.psp.model.dto.PaymentIntentResponse;
import com.stitch.psp.model.dto.stripe.CustomerRequest;
import com.stitch.psp.model.dto.stripe.StripeVerificationResponse;
import com.stitch.psp.model.entity.PspCustomerInfo;
import com.stitch.psp.model.enums.PaymentProvider;
import com.stitch.psp.repository.PspCustomerInfoRepository;
import com.stitch.psp.service.ForeignPaymentProvider;
import com.stitch.psp.service.StripeApi;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.EphemeralKey;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.EphemeralKeyCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Slf4j
@Service("stripe")
public class StripeService implements StripeApi, ForeignPaymentProvider {

    private StripeClient stripeClient;
    private final PspCustomerInfoRepository pspCustomerInfoRepository;

    private final String API_VERSION = "2023-08-16";

    @Value("${psp.stripe.secret-key}")
    private String SECRET_KEY;

    @Value("${psp.stripe.publishable-key}")
    private String PUBLISHABLE_KEY;

    public StripeService(PspCustomerInfoRepository pspCustomerInfoRepository) {
        this.pspCustomerInfoRepository = pspCustomerInfoRepository;
    }

    @PostConstruct
    public void setupClient() {
        this.stripeClient = new StripeClient(SECRET_KEY);
    }


    @Override
    public PaymentIntentResponse createPaymentIntent(PaymentIntentRequest paymentIntentRequest) {

        log.debug("Creating Stripe payment intent: {}", paymentIntentRequest);

        PspCustomerInfo customerInfo;
        Optional<PspCustomerInfo> optionalStripeCustomerInfo = pspCustomerInfoRepository.findByBillantedCustomerIdAndPaymentProvider(paymentIntentRequest.getCustomerId(), PaymentProvider.STRIPE);

        if (optionalStripeCustomerInfo.isPresent()) {
            customerInfo = optionalStripeCustomerInfo.get();
        } else {

            CustomerRequest customerRequest = CustomerRequest.builder()
                    .customerId(paymentIntentRequest.getCustomerId())
                    .emailAddress(paymentIntentRequest.getEmailAddress())
                    .fullName(paymentIntentRequest.getFullName())
                    .build();
            customerInfo = createCustomer(customerRequest);
        }

        long amount = (long) (paymentIntentRequest.getAmount().doubleValue() * 100);

        if (amount < 100){
            throw new PaymentServiceException(String.format("Minimum amount required is equivalent of %s1.00", paymentIntentRequest.getCurrency()));
        }

        PaymentIntentCreateParams.Builder paramsBuilder = PaymentIntentCreateParams.builder()
                .setCurrency(paymentIntentRequest.getCurrency().name().toLowerCase())
                .setAmount(amount)
                .setCustomer(customerInfo.getPspCustomerId())
                .addAllPaymentMethodType(List.of("card"));

        if (paymentIntentRequest.isSavePaymentCard()) {
            paramsBuilder.setSetupFutureUsage(PaymentIntentCreateParams.SetupFutureUsage.ON_SESSION);
        }


        try {
            PaymentIntent paymentIntent = stripeClient.paymentIntents().create(paramsBuilder.build());
            log.debug("Created payment intent: {}", paymentIntent);

            return PaymentIntentResponse.
                    builder()
                    .paymentId(paymentIntent.getId())
                    .clientSecret(paymentIntent.getClientSecret())
                    .providerCustomerId(customerInfo.getPspCustomerId())
                    .amount(paymentIntentRequest.getAmount())
                    .currency(paymentIntent.getCurrency())
                    .build();

        } catch (StripeException e) {
            log.error("Error calling Stripe API to create payment intent", e);
            throw new PaymentServiceException("Failed to initiate payment", e);
        }
    }

    @Override
    public PaymentIntent makeCardPayment(PaymentIntentRequest paymentIntentRequest) {

        log.debug("Making Stripe card payment: {}", paymentIntentRequest);

        PspCustomerInfo customerInfo;
        Optional<PspCustomerInfo> optionalStripeCustomerInfo = pspCustomerInfoRepository.findByBillantedCustomerIdAndPaymentProvider(paymentIntentRequest.getCustomerId(), PaymentProvider.STRIPE);

        if (optionalStripeCustomerInfo.isPresent()) {
            customerInfo = optionalStripeCustomerInfo.get();
        } else {

            CustomerRequest customerRequest = CustomerRequest.builder()
                    .customerId(paymentIntentRequest.getCustomerId())
                    .emailAddress(paymentIntentRequest.getEmailAddress())
                    .fullName(paymentIntentRequest.getFullName())
                    .build();
            customerInfo = createCustomer(customerRequest);
        }

        long amount = (long) (paymentIntentRequest.getAmount().doubleValue() * 100);

        if (amount < 100){
            throw new PaymentServiceException(String.format("Minimum amount required is equivalent of %s1.00", paymentIntentRequest.getCurrency()));
        }
        PaymentIntentCreateParams.Builder paramsBuilder = PaymentIntentCreateParams.builder()
                .setCurrency(paymentIntentRequest.getCurrency().name().toLowerCase())
                .setAmount(amount)
                .setCustomer(customerInfo.getPspCustomerId())
                .addAllPaymentMethodType(List.of("card"))
                .setConfirm(true)
                .setPaymentMethod(paymentIntentRequest.getPaymentMethodId());


        try {
            PaymentIntent paymentIntent = stripeClient.paymentIntents().create(paramsBuilder.build());
            log.debug("Confirmed payment intent creation response: {}", paymentIntent);

            return paymentIntent;

        } catch (StripeException e) {
            log.error("Error calling Stripe API to create and confirm payment intent", e);
            throw new PaymentServiceException("Failed to make payment", e);
        }
    }

    @Override
    public StripeVerificationResponse verifyPayment(String paymentId) throws StripeException {

        log.debug("Verifying Stripe payment for paymentId: {}", paymentId);

            PaymentIntent paymentIntent = stripeClient.paymentIntents().retrieve(paymentId);
            log.debug("Retrieved Stripe payment intent: {}", paymentIntent);

            StripeVerificationResponse.StripeVerificationResponseBuilder verificationResponseBuilder = StripeVerificationResponse.builder()
                    .paymentId(paymentIntent.getId())
                    .status(paymentIntent.getStatus())
                    .currency(paymentIntent.getCurrency())
                    .amount(paymentIntent.getAmount())
                    .rawResponse(paymentIntent.toJson());

            String paymentMethodId = paymentIntent.getPaymentMethod();
            if (paymentMethodId != null) {

                PaymentMethod paymentMethod = stripeClient.paymentMethods().retrieve(paymentMethodId);
                verificationResponseBuilder.paymentMethodId(paymentMethodId);
                if (paymentMethod.getCard() != null) {
                    PaymentMethod.Card card = paymentMethod.getCard();
                    verificationResponseBuilder.last4digits(card.getLast4())
                            .cardType(card.getBrand())
                            .country(card.getCountry())
                            .cardExpMonth(card.getExpMonth())
                            .cardExpYear(card.getExpYear())
                            .cardFingerprint(card.getFingerprint());
                }
            }
            return verificationResponseBuilder.build();
    }

    public PspCustomerInfo createCustomer(CustomerRequest customerRequest) {

        log.debug("Creating Stripe customer: {}", customerRequest);

        CustomerCreateParams params =
                CustomerCreateParams
                        .builder()
                        .setEmail(customerRequest.getEmailAddress())
                        .setName(customerRequest.getFullName())
                        .build();

        try {
            Customer customer = stripeClient.customers().create(params);
            log.debug("Created Stripe customer: {}", customer);

            PspCustomerInfo customerInfo = PspCustomerInfo.
                    builder().pspCustomerId(customer.getId())
                    .emailAddress(customer.getEmail())
                    .billantedCustomerId(customerRequest.getCustomerId())
                    .paymentProvider(PaymentProvider.STRIPE)
                    .build();

            customerInfo = pspCustomerInfoRepository.saveAndFlush(customerInfo);
            return customerInfo;
        } catch (StripeException e) {
            log.error("Error calling Stripe API to create customer", e);
            throw new PaymentServiceException("Failed to create Stripe customer", e);
        } catch (Exception e) {
            log.error("Error saving created Stripe customer", e);
            throw new PaymentServiceException("Failed to save new Stripe customer", e);
        }
    }


    private EphemeralKey createEphemeralKey(String stripeCustomerId) {

        log.debug("Creating ephemeral key for stripe customerId: {}", stripeCustomerId);

        EphemeralKeyCreateParams params = EphemeralKeyCreateParams.builder()
                .setStripeVersion(API_VERSION)
                .setCustomer(stripeCustomerId)
                .build();
        try {
            EphemeralKey ephemeralKey = stripeClient.ephemeralKeys().create(params);
            log.debug("Created Stripe ephemeral key: {}", ephemeralKey);

            return ephemeralKey;
        } catch (StripeException e) {
            log.error("Error creating ephemeral key for Stripe customer", e);
            throw new PaymentServiceException("Failed to create customer ephemeral key for Stripe transaction", e);
        }
    }

}
