package com.stitch.payment.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.stitch.commons.exception.StitchException;
import com.stitch.commons.util.NumberUtils;
import com.stitch.model.dto.ProductOrderDto;
import com.stitch.model.dto.ProductOrderRequest;
import com.stitch.model.entity.ProductCart;
import com.stitch.model.entity.ProductOrder;
import com.stitch.model.enums.OrderStatus;
import com.stitch.model.enums.PaymentMode;
import com.stitch.payment.model.entity.Transaction;
import com.stitch.payment.model.dto.PaymentVerificationResponse;
import com.stitch.payment.model.entity.InitializeTransactionRequest;
import com.stitch.payment.model.entity.InitializeTransactionResponse;
//import com.stitch.payment.model.entity.Transaction;
import com.stitch.payment.model.enums.TransactionStatus;
import com.stitch.payment.service.PaymentService;
import com.stitch.payment.service.TransactionService;
import com.stitch.repository.ProductCartRepository;
import com.stitch.repository.ProductOrderRepository;
import com.stitch.service.ProductOrderService;
import com.stitch.user.model.entity.UserEntity;
import com.stitch.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
//import java.net.http.HttpClient;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
//import java.net.http.HttpResponse;


//@Slf4j
//@Service
//public class PaymentServiceImpl implements PaymentService {
//
//    private final LocalPaymentProviderService localPaymentProviderService;
//    private final ForeignPaymentProviderService foreignPaymentProviderService;
//    private final WalletService walletService;
//    private final PaymentCardService paymentCardService;
//    private final TransactionService transactionService;
//    private final ExchangeRateService exchangeRateService;
//    private final PaymentReversalRequestRepository reversalRequestRepository;
//
//
//    public PaymentServiceImpl(LocalPaymentProviderService localPaymentProviderService, ForeignPaymentProviderService foreignPaymentProviderService, WalletService walletService, PaymentCardService paymentCardService, TransactionService transactionService, ExchangeRateService exchangeRateService, PaymentReversalRequestRepository reversalRequestRepository) {
//        this.localPaymentProviderService = localPaymentProviderService;
//        this.foreignPaymentProviderService = foreignPaymentProviderService;
//        this.walletService = walletService;
//        this.paymentCardService = paymentCardService;
//        this.transactionService = transactionService;
//        this.exchangeRateService = exchangeRateService;
//        this.reversalRequestRepository = reversalRequestRepository;
//    }
//
//    @Override
//    public PaymentVerificationResponse verifyFundingTransaction(String customerId, boolean saveCard, PaymentVerificationRequest paymentVerificationRequest) {
//
//        PaymentVerificationResponse paymentVerificationResponse;
//        if (Currency.NGN.name().equalsIgnoreCase(paymentVerificationRequest.getCurrency())) {
//            paymentVerificationResponse = localPaymentProviderService.verifyTransaction(paymentVerificationRequest);
//        } else {
//            paymentVerificationResponse = foreignPaymentProviderService.verifyTransaction(paymentVerificationRequest);
//        }
//        if (!paymentVerificationResponse.getStatus().equals(VerificationStatus.SUCCESSFUL)) {
//            throw new PaymentFailedException("Funding with card failed");
//        }
//        saveCard(customerId, saveCard, paymentVerificationResponse);
//        return paymentVerificationResponse;
//    }
//
//    @Override
//    public PaymentVerificationResponse verifyOrderTransaction(String customerId, boolean saveCard, PaymentVerificationRequest paymentVerificationRequest) {
//
//        PaymentVerificationResponse paymentVerificationResponse;
//        if (Currency.NGN.name().equalsIgnoreCase(paymentVerificationRequest.getCurrency())) {
//            paymentVerificationResponse = localPaymentProviderService.verifyTransaction(paymentVerificationRequest);
//        } else {
//            BigDecimal amount = exchangeRateService.getEquivalentCurrencyAmount(Currency.valueOf(paymentVerificationRequest.getCurrency()), paymentVerificationRequest.getAmount());
//            paymentVerificationRequest.setAmount(amount);
//            paymentVerificationResponse = foreignPaymentProviderService.verifyTransaction(paymentVerificationRequest);
//        }
//        if (!paymentVerificationResponse.getStatus().equals(VerificationStatus.SUCCESSFUL)) {
//            throw new PaymentFailedException("Payment with card failed");
//        }
//        saveCard(customerId, saveCard, paymentVerificationResponse);
//
//        return paymentVerificationResponse;
//    }
//
//    @Override
//    public PaymentVerificationResponse chargeCard(CardPaymentRequest cardPaymentRequest, boolean saveCard) {
//        PaymentVerificationResponse paymentVerificationResponse = chargeCard(cardPaymentRequest);
//        saveCard(cardPaymentRequest.getCustomerId(), saveCard, paymentVerificationResponse);
//        return paymentVerificationResponse;
//    }
//
//    private void saveCard(String customerId, boolean saveCard, PaymentVerificationResponse paymentVerificationResponse) {
//        if (saveCard && paymentVerificationResponse.getStatus().equals(VerificationStatus.SUCCESSFUL)) {
//            paymentCardService.saveCard(customerId, paymentVerificationResponse);
//        }
//    }
//
//
//    @Override
//    public PaymentResponse debitWallet(WalletDebitRequest walletDebitRequest) {
//
//        try {
//            WalletTransactionDto transactionDto = walletService.debitCustomerWallet(walletDebitRequest);
//            PaymentResponse paymentResponse = new PaymentResponse();
//            paymentResponse.setTransactionRefId(transactionDto.getTransactionId());
//            return paymentResponse;
//        } catch (WalletException e) {
//            throw new PaymentException(e.getMessage(), e);
//        }
//    }
//
//
//    @Async
//    @Override
//    public void initiatePaymentReversal(PaymentReversalRequest paymentReversalRequest) {
//        log.debug("Initiating payment reversal: {}", paymentReversalRequest);
//
//        paymentReversalRequest.setStatus(ReversalStatus.PENDING);
//
//        PaymentReversalRequest reversalRequest = reversalRequestRepository.saveAndFlush(paymentReversalRequest);
//
//        if (reversalRequest.getTransactionId() != null) {
//            final Transaction transaction = transactionService.retrieveTransaction(reversalRequest.getTransactionId());
//            if (reversalRequest.getPaymentMode().equals(PaymentMode.WALLET)) {
//
//                final WalletDebitReversalRequest debitReversalRequest = WalletDebitReversalRequest.builder()
//                        .walletTransactionId(transaction.getWalletTransactionId())
//                        .walletId(transaction.getWalletId())
//                        .customerId(transaction.getCustomerId())
//                        .amount(transaction.getSrcAmount())
//                        .currency(transaction.getSrcCurrency())
//                        .description(String.format("RVSL: %s: %s", transaction.getTransactionId(), transaction.getProductCategory()))
//                        .build();
//                try {
//                    walletService.reverseWalletDebitTransaction(debitReversalRequest);
//                    reversalRequest.setStatus(ReversalStatus.COMPLETED);
//                    log.info("Successfully reversed wallet debit for bill payment");
//                } catch (WalletException e) {
//                    log.error("Error reversing wallet debit: {}", debitReversalRequest, e);
//                    reversalRequest.setStatus(ReversalStatus.FAILED);
//                    reversalRequest.setComments(e.getMessage());
//                } finally {
//                    paymentReversalRequest.setDescription(String.format("RVSL: %s", transaction.getProductCategory()));
//                    reversalRequestRepository.saveAndFlush(paymentReversalRequest);
//                }
//            } else if (reversalRequest.getPaymentMode().equals(PaymentMode.CARD)) {
//
//
//                final String walletId = walletService.getCustomerDefaultWalletId(transaction.getCustomerId());
//
//                final WalletCreditRequest creditRequest = WalletCreditRequest.builder()
//                        .walletId(walletId)
//                        .customerId(transaction.getCustomerId())
//                        .amount(transaction.getSrcAmount())
//                        .currency(transaction.getSrcCurrency())
////                        .transactionType(TransactionType.FUND_WALLET())
//                        .description(String.format("RVSL: %s: %s", transaction.getTransactionId(), transaction.getProductCategory()))
//                        .build();
//
//                try {
//                    WalletTransactionDto walletTransaction = walletService.creditCustomerWallet(creditRequest);
//                    reversalRequest.setWalletId(walletId);
//                    reversalRequest.setStatus(ReversalStatus.COMPLETED);
//
//                    TransactionRequest transactionRequest = TransactionRequest.builder()
//                            .amount(transaction.getSrcAmount())
//                            .currency(transaction.getSrcCurrency())
//                            .orderId(transaction.getOrderId())
//                            .transactionType(TransactionType.FUND_WALLET)
//                            .paymentMode(transaction.getPaymentMode())
//                            .customerId(transaction.getCustomerId())
//                            .walletId(walletId)
//                            .walletTransactionId(walletTransaction.getTransactionId())
//                            .cardId(transaction.getPaymentCardId())
//                            .srcCurrency(transaction.getSrcCurrency())
//                            .srcAmount(transaction.getSrcAmount())
//                            .destCurrency(transaction.getDestCurrency())
//                            .destAmount(transaction.getDestAmount())
//                            .exchangeRate(transaction.getExchangeRate())
//                            .description(String.format("RVSL: %s: %s", transaction.getTransactionId(), transaction.getProductCategory()))
//                            .fee(BigDecimal.ZERO)
//                            .status(TransactionStatus.C)
//                            .build();
//                    transactionService.createTransaction(transactionRequest);
//                    log.info("Successfully credited customer via reversal of card debit for bill payment");
//                } catch (WalletException e) {
//                    log.error("Error crediting customer via reversal of card debit: {}", creditRequest, e);
//                    reversalRequest.setStatus(ReversalStatus.FAILED);
//                    reversalRequest.setComments(e.getMessage());
//                } finally {
//                    paymentReversalRequest.setDescription(String.format("RVSL: %s", transaction.getProductCategory()));
//                    reversalRequestRepository.saveAndFlush(paymentReversalRequest);
//                }
//            }
//        } else {
//            log.warn("Cannot process auto-reversal for non-recorded transaction. Consider an alternative reconciliation process");
//        }
//    }
//
//
//    @Override
//    public PaymentVerificationResponse chargeCard(CardPaymentRequest cardPaymentRequest) {
//
//        if (cardPaymentRequest.getDestCurrency() == null) {
//            Currency currency = getCustomerCurrency(cardPaymentRequest.getCustomerId());
//            cardPaymentRequest.setDestCurrency(currency);
//        }
//        PaymentVerificationResponse paymentVerificationResponse = paymentCardService.chargeCard(cardPaymentRequest);
//
//        if (paymentVerificationResponse != null && !paymentVerificationResponse.getStatus().equals(VerificationStatus.SUCCESSFUL)) {
//            throw new PaymentFailedException("Card Payment Failed");
//        }
//        return paymentVerificationResponse;
//    }
//
//
//    @Override
//    public List<CardPaymentResponse> getCustomerCards(String customerId) {
//        return paymentCardService.getCustomerCards(customerId);
//    }
//
//
//    @Override
//    public Transaction addTransaction(TransactionRequest transactionRequest) {
//
//        Currency customerCurrency = getCustomerCurrency(transactionRequest.getCustomerId());
//        transactionRequest.setSrcCurrency(customerCurrency);
//
//        if (!customerCurrency.equals(Currency.NGN)) {
//            Exchange exchange = exchangeRateService.getExchange(customerCurrency, transactionRequest.getAmount());
//            transactionRequest.setSrcAmount(exchange.getTotalAmount());
//            transactionRequest.setExchangeRate(exchange.getRate());
//        }
//        return transactionService.createTransaction(transactionRequest);
//    }
//
//    @Override
//    public void updateTransactionStatus(String transactionId, TransactionStatus transactionStatus) {
//        transactionService.updateTransactionStatus(transactionId, transactionStatus);
//    }
//
//    @Override
//    public void updateTransaction(Transaction transaction) {
//        transactionService.updateTransaction(transaction);
//    }
//
//
//    @Override
//    public PaymentIntentResponse createPaymentIntent(PaymentIntentRequest paymentIntentRequest) {
//
//        try {
//            return foreignPaymentProviderService.createPaymentIntent(paymentIntentRequest);
//        } catch (PaymentServiceException e) {
//            throw new PaymentException(e.getMessage(), e);
//        }
//    }
//
//    @Override
//    public Currency getCustomerCurrency(String customerId) {
//        return walletService.getCustomerCurrency(customerId);
//    }
//
//    @Override
//    public PaymentResponse initiateForeignPayment(PaymentRequest paymentRequest) {
//
//        PaymentIntentRequest paymentIntentRequest = PaymentIntentRequest.builder()
//                .currency(paymentRequest.getDestCurrency())
//                .customerId(paymentRequest.getCustomerId())
//                .emailAddress(paymentRequest.getEmailAddress())
//                .fullName(paymentRequest.getFullName())
//                .savePaymentCard(paymentRequest.isSavePaymentCard())
//                .build();
//
//        BigDecimal amount = exchangeRateService.getEquivalentCurrencyAmount(paymentRequest.getDestCurrency(), paymentRequest.getAmount());
//        paymentIntentRequest.setAmount(amount);
//
//        try {
//            PaymentIntentResponse paymentIntent = foreignPaymentProviderService.createPaymentIntent(paymentIntentRequest);
//            PaymentResponse paymentResponse = new PaymentResponse();
//            paymentResponse.setClientSecret(paymentIntent.getClientSecret());
//            paymentResponse.setTransactionRefId(paymentIntent.getPaymentId());
//            return paymentResponse;
//
//        } catch (PaymentServiceException e) {
//            throw new PaymentException(e.getMessage(), e);
//        }
//    }
//
//    @Override
//    public Response deleteCard(String customerId, String cardId) {
//        return paymentCardService.deleteCard(customerId, cardId);
//    }
//
//    @Override
//    public TransactionDetailsDto fetchPaymentDetails(String transactionId, String customerId) {
//        return transactionService.fetchPaymentDetails(transactionId, customerId);
//    }
//    @Override
//    public Transaction retrieveTransactionByOrderId(String orderId) {
//        return transactionService.retrieveTransaction(orderId);
//    }
//}


@Slf4j
@Service
//@Configuration
public class PaymentServiceImpl implements PaymentService{


    @Value("${paystack.secret-key}")
    private String paystackSecretKey;

    @Value("${paystack.initialize-payment-url}")
    private String initializePaymentUrl;

    @Value("${paystack.call-back-url}")
    private String callBackURL;

    @Value("${paystack.verification-url}")
    private String paystackVerificationUrl;


    private final  ProductOrderService productOrderService;

    private final TransactionService transactionService;

    private final ProductOrderService orderService;

    private final ProductOrderRepository productOrderRepository;

    private final ProductCartRepository productCartRepository;

    private final UserRepository userRepository;

    public PaymentServiceImpl(ProductOrderService productOrderService, TransactionService transactionService, ProductOrderService orderService, ProductOrderRepository productOrderRepository, ProductCartRepository productCartRepository, UserRepository userRepository) {
        this.productOrderService = productOrderService;
        this.transactionService = transactionService;
        this.orderService = orderService;
        this.productOrderRepository = productOrderRepository;
        this.productCartRepository = productCartRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void initializePayment(ProductOrder order) {

    }

    public void initializeOrder(InitializeTransactionRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Optional<UserEntity> customerOptional = userRepository.findByEmailAddress(email);
        if(customerOptional.isEmpty()){
            throw new StitchException("Customer with Id : " + email + " does not exist");
        }
        UserEntity customer = customerOptional.get();

        List<ProductCart> productCart = productCartRepository.findProductCartByCustomer(customer);
            log.info(" productCarts ---->>>: {}", productCart);

        String transactionId = request.getReference();

        for(ProductCart productItem: productCart){
            ProductOrderRequest productOrderRequest = new ProductOrderRequest();
            productOrderRequest.setStatus(OrderStatus.PROCESSING.toString());
            productOrderRequest.setEmailAddress(email);
            productOrderRequest.setTransactionId(transactionId);
            productOrderRequest.setOrderId(NumberUtils.generate(10) + productItem.getProductId());
            productOrderRequest.setAmount(productItem.getAmountByQuantity());
            productOrderRequest.setProductId(productItem.getProductId());
            productOrderRequest.setProductCategoryName(productItem.getProductCategoryName());
            productOrderRequest.setPaymentMode(PaymentMode.CARD);
            productOrderRequest.setNarration(request.getNarration());
            productOrderRequest.setVendorEmailAddress(productItem.getVendor().getEmailAddress());
            productOrderRequest.setQuantity(BigDecimal.valueOf(productItem.getQuantity()));
            productOrderRequest.setColor(productItem.getColor());
            productOrderRequest.setSleeveType(productItem.getSleeveType());
            productOrderRequest.setBodyMeasurementTag(productItem.getMeasurementTag());


            ProductOrderDto productOrder = productOrderService.createProductOrder(productOrderRequest);

            log.info("saved productOrder : {}", productOrder);
        }
    }

    @Override
    public InitializeTransactionResponse initTransaction(InitializeTransactionRequest request) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        InitializeTransactionResponse initializeTransactionResponse = null;
        try {
            // convert transaction to json then use it as a body to post json
            Gson gson = new Gson();

            request.setEmail(email);
            request.setReference(NumberUtils.generate(10));
            request.setChannel(request.getChannel());
            request.setTransaction_charge(2);
            request.setCallback_url(callBackURL);
            request.setAmount(request.getAmount().multiply(BigDecimal.valueOf(100)));
            StringEntity postingString = new StringEntity(gson.toJson(request));
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(initializePaymentUrl);
            post.setEntity(postingString);
            post.addHeader("Content-type", "application/json");
            post.addHeader("Authorization", "Bearer " + paystackSecretKey);
            StringBuilder result = new StringBuilder();
            HttpResponse response = client.execute(post);

            log.info("PayStack response : {}", response);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                System.out.println("Response Body: " + result.toString());

            } else {
                throw new StitchException("Error Occurred while initializing transaction");
            }
            ObjectMapper mapper = new ObjectMapper();

            initializeTransactionResponse = mapper.readValue(result.toString(), InitializeTransactionResponse.class);
            log.info("initializeTransactionResponse : {}", initializeTransactionResponse);

            initializeOrder(request);

            if(initializeTransactionResponse.isStatus()){
                paymentVerification(initializeTransactionResponse.getData().getReference());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Failure initializing paystack transaction: " + ex.getMessage() );
        }

        return initializeTransactionResponse;
    }

    @Override
//    @Transactional
    public PaymentVerificationResponse paymentVerification(String reference) throws Exception {
        PaymentVerificationResponse paymentVerificationResponse = null;
        Transaction transaction = null;

        try{
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(paystackVerificationUrl + reference);
            request.addHeader("Content-type", "application/json");
            request.addHeader("Authorization", "Bearer " + paystackSecretKey);
            StringBuilder result = new StringBuilder();
            HttpResponse response = client.execute(request);

            if (response.getStatusLine(). getStatusCode() == HttpStatus.SC_OK) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
            } else {
                throw new Exception("Paystack is unable to verify payment at the moment");
            }

            ObjectMapper mapper = new ObjectMapper();
            paymentVerificationResponse = mapper.readValue(result.toString(), PaymentVerificationResponse.class);

            System.out.println("verify " + result);

            if (paymentVerificationResponse == null || !paymentVerificationResponse.getStatus()) {
                throw new Exception("An error");
            } else if (paymentVerificationResponse.getMessage().equals("Verification successful")) {

                transaction = Transaction.builder()
                        .userId(paymentVerificationResponse.getData().getCustomer().getEmail())
                        .transactionId(paymentVerificationResponse.getData().getReference())
                        .reference(paymentVerificationResponse.getData().getReference())
                        .amount(paymentVerificationResponse.getData().getAmount().divide(BigDecimal.valueOf(100)))
                        .gatewayResponse(paymentVerificationResponse.getData().getGatewayResponse())
                        .paidAt(paymentVerificationResponse.getData().getPaidAt())
                        .createdAt(paymentVerificationResponse.getData().getCreatedAt())
                        .channel(paymentVerificationResponse.getData().getChannel())
                        .currency(paymentVerificationResponse.getData().getCurrency())
                        .ipAddress(paymentVerificationResponse.getData().getIpAddress())
                        .build();
                if(paymentVerificationResponse.getData().getStatus().equals("abandoned")){
                    System.out.println("ABANDONED HERE");
                    transaction.setStatus(TransactionStatus.FAILED);
                }

                if(paymentVerificationResponse.getData().getStatus().equals("success")){
                    System.out.println("SUCCESS HERE");

                    transaction.setStatus(TransactionStatus.COMPLETED);
                }

                Transaction t = transactionService.saveTransaction(transaction);

                log.info("transaction entity: {}", t);

                List<ProductOrder> productOrders = orderService.getOrdersByTransactionId(t.getTransactionId());

                log.info("productOrder entity: {}", productOrders);

                for(ProductOrder productOrder: productOrders){
                    productOrder.setStatus(OrderStatus.PAYMENT_COMPLETED);
                    ProductOrder p = productOrderRepository.save(productOrder);
                    log.info("product order entity: {}", p);
                }

            }
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
        return paymentVerificationResponse;
    }
}