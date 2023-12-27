//package com.stitch.gateway.controller.wallet;
//
//
//import com.exquisapps.billanted.currency.model.enums.Currency;
//import com.exquisapps.billanted.gateway.security.service.AuthenticationService;
//import com.exquisapps.billanted.payment.model.dto.CardPaymentRequest;
//import com.exquisapps.billanted.payment.model.dto.DailyLimitRequest;
//import com.exquisapps.billanted.payment.model.enums.PaymentMode;
//import com.exquisapps.billanted.payment.model.enums.TierActionType;
//import com.exquisapps.billanted.payment.model.enums.TransactionType;
//import com.exquisapps.billanted.payment.service.PaymentService;
//import com.exquisapps.billanted.payment.service.TierConfigService;
//import com.exquisapps.billanted.psp.model.PaymentVerificationRequest;
//import com.exquisapps.billanted.psp.model.PaymentVerificationResponse;
//import com.exquisapps.billanted.psp.model.dto.PaymentIntentRequest;
//import com.exquisapps.billanted.psp.model.dto.PaymentIntentResponse;
//import com.exquisapps.billanted.user.model.dto.CustomerDto;
//import com.exquisapps.billanted.user.service.CustomerService;
//import com.exquisapps.billanted.wallet.model.dto.TransactionRequest;
//import com.exquisapps.billanted.wallet.model.dto.WalletDto;
//import com.exquisapps.billanted.wallet.model.dto.WalletTransactionRequestDto;
//import com.exquisapps.billanted.wallet.model.enums.TransactionStatus;
//import com.exquisapps.billanted.wallet.service.WalletService;
//import org.springframework.graphql.data.method.annotation.Argument;
//import org.springframework.graphql.data.method.annotation.MutationMapping;
//import org.springframework.graphql.data.method.annotation.QueryMapping;
//import org.springframework.stereotype.Controller;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//@Controller
//public class WalletController {
//
//    private final WalletService walletService;
//    private final AuthenticationService authenticationService;
//    private final CustomerService customerService;
//    private final PaymentService paymentService;
//
//    private final TierConfigService tierConfigService;
//
//    public WalletController(WalletService walletService, AuthenticationService authenticationService,
//                            CustomerService customerService, PaymentService paymentService, TierConfigService tierConfigService) {
//        this.walletService = walletService;
//        this.authenticationService = authenticationService;
//        this.customerService = customerService;
//        this.paymentService = paymentService;
//        this.tierConfigService = tierConfigService;
//    }
//
//    @QueryMapping(value = "getWallets")
//    public List<WalletDto> getAllCustomerWallets() {
//        String customerId = authenticationService.getAuthenticatedCustomerId();
//        return walletService.getAllWallets(customerId);
//    }
//
//
//    @MutationMapping(value = "initiateFunding")
//    public WalletTransactionRequestDto initiateFunding(@Argument("transactionRequest") TransactionRequest transactionRequest) {
//
//        CustomerDto user = authenticationService.getAuthenticatedUser();
//        String customerId = user.getCustomerId();
//
//        customerService.checkPin(customerId, transactionRequest.getPin());
//        transactionRequest.setCustomerId(customerId);
//
//        Currency currency = walletService.getCustomerCurrency(customerId);
//
//        tierConfigService.cumulativeDailyLimits(DailyLimitRequest.builder()
//            .currency(currency)
//            .actionType(TierActionType.F)
//            .customerId(customerId)
//            .loadAmount(transactionRequest.getAmount())
//            .tier(user.getTier())
//            .build());
//
//        WalletTransactionRequestDto transactionRequestDto = walletService.initiateFunding(transactionRequest);
//        if (!transactionRequestDto.getCurrency().equalsIgnoreCase(Currency.NGN.name())) {
//            // Wallet funding is for foreign currency
//            PaymentIntentRequest paymentIntentRequest = PaymentIntentRequest.builder()
//                    .amount(transactionRequestDto.getAmount())
//                    .currency(Currency.valueOf(transactionRequestDto.getCurrency()))
//                    .customerId(customerId)
//                    .emailAddress(user.getEmailAddress())
//                    .fullName(user.getFirstName() + " " + user.getLastName())
//                    .savePaymentCard(user.isSaveCard())
//                    .build();
//            PaymentIntentResponse paymentIntent = paymentService.createPaymentIntent(paymentIntentRequest);
//            transactionRequestDto.setPaymentId(paymentIntent.getPaymentId());
//            transactionRequestDto.setClientSecret(paymentIntent.getClientSecret());
//        }
//        return transactionRequestDto;
//    }
//
//    @MutationMapping(value = "cancelFunding")
//    public WalletTransactionRequestDto cancelFunding(@Argument("transactionId") String transactionId) {
//        return walletService.cancelFunding(transactionId);
//    }
//
//    @MutationMapping(value = "verifyFunding")
//    public WalletTransactionRequestDto verifyWalletFunding(@Argument("transactionVerificationRequest") TransactionRequest transactionRequest) {
//
//        CustomerDto customer = authenticationService.getAuthenticatedUser();
//
//        PaymentVerificationRequest paymentVerificationRequest = new PaymentVerificationRequest();
//        paymentVerificationRequest.setTransactionRefId(transactionRequest.getTxRef());
//        paymentVerificationRequest.setAmount(transactionRequest.getAmount());
//        paymentVerificationRequest.setCurrency(transactionRequest.getCurrency());
//
//        PaymentVerificationResponse paymentVerificationResponse = paymentService.verifyFundingTransaction(customer.getCustomerId(), transactionRequest.isSaveCard(), paymentVerificationRequest);
//        transactionRequest.setStatus(TransactionStatus.S);
//        transactionRequest.setFirstName(customer.getFirstName());
//        transactionRequest.setEmailAddress(customer.getEmailAddress());
//        transactionRequest.setCustomerId(customer.getCustomerId());
//
//        WalletTransactionRequestDto walletTransactionRequestDto = walletService.processFunding(transactionRequest);
//        walletTransactionRequestDto.setPaymentId(paymentVerificationResponse.getTransactionRefId());
//        paymentService.addTransaction(createTransactionRequest(walletTransactionRequestDto));
//        return walletTransactionRequestDto;
//    }
//
//
//    @MutationMapping(value = "fundWallet")
//    public WalletTransactionRequestDto fundWallet(@Argument("transactionRequest") TransactionRequest transactionRequest) {
//
//        CustomerDto user = authenticationService.getAuthenticatedUser();
//        String customerId = user.getCustomerId();
//
//        Currency currency = walletService.getCustomerCurrency(customerId);
//
//        tierConfigService.cumulativeDailyLimits(DailyLimitRequest.builder()
//            .currency(currency)
//            .actionType(TierActionType.F)
//            .customerId(customerId)
//            .loadAmount(transactionRequest.getAmount())
//            .tier(user.getTier())
//            .build());
//
//        customerService.checkPin(customerId, transactionRequest.getPin());
//        transactionRequest.setCustomerId(customerId);
//        transactionRequest.setEnablePush(user.isEnablePush());
//        transactionRequest.setFirstName(user.getFirstName());
//        transactionRequest.setEmailAddress(user.getEmailAddress());
//
//        WalletTransactionRequestDto transactionRequestDto = walletService.initiateFunding(transactionRequest);
//        transactionRequest.setTransactionId(transactionRequestDto.getTransactionId());
//
//        CardPaymentRequest cardPaymentRequest = CardPaymentRequest.builder()
//                .cardId(transactionRequest.getCardId())
//                .email(user.getEmailAddress())
//                .customerId(customerId)
//                .narration(transactionRequest.getDescription())
//                .amount(transactionRequest.getAmount())
//                .srcCurrency(Currency.valueOf(transactionRequest.getCurrency()))
//                .destCurrency(Currency.valueOf(transactionRequest.getCurrency()))
//                .build();
//
//        PaymentVerificationResponse paymentVerificationResponse = paymentService.chargeCard(cardPaymentRequest);
//
//        transactionRequest.setStatus(TransactionStatus.S);
//        WalletTransactionRequestDto walletTransactionRequestDto = walletService.processFunding(transactionRequest);
//        walletTransactionRequestDto.setCardId(cardPaymentRequest.getCardId());
//        walletTransactionRequestDto.setPaymentId(paymentVerificationResponse.getTransactionRefId());
//        paymentService.addTransaction(createTransactionRequest(walletTransactionRequestDto));
//        return walletTransactionRequestDto;
//    }
//
//    private com.exquisapps.billanted.payment.model.dto.TransactionRequest createTransactionRequest(WalletTransactionRequestDto transactionDto) {
//        return com.exquisapps.billanted.payment.model.dto.TransactionRequest.builder()
//                .paymentMode(PaymentMode.CARD)
//                .cardId(transactionDto.getCardId())
//                .cardTransactionId(transactionDto.getPaymentId())
//                .walletTransactionId(transactionDto.getTransactionId())
//                .customerId(transactionDto.getCustomerId())
//                .walletId(transactionDto.getWalletId())
//                .amount(transactionDto.getAmount())
//                .currency(Currency.valueOf(transactionDto.getCurrency()))
//                .fee(BigDecimal.ZERO)
//                .transactionType(TransactionType.FUND_WALLET)
//                .srcAmount(transactionDto.getAmount())
//                .srcCurrency(Currency.valueOf(transactionDto.getCurrency()))
//                .destAmount(transactionDto.getAmount())
//                .destCurrency(Currency.valueOf(transactionDto.getCurrency()))
//                .status(com.exquisapps.billanted.payment.model.enums.TransactionStatus.C)
//                .build();
//    }
//}
