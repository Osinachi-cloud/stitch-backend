package com.stitch.payment.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import com.google.gson.Gson;
import com.stitch.commons.util.NumberUtils;
import com.stitch.model.dto.ProductOrderDto;
import com.stitch.model.dto.ProductOrderRequest;
import com.stitch.model.entity.ProductCart;
import com.stitch.model.entity.ProductOrder;
import com.stitch.model.enums.OrderStatus;
import com.stitch.model.enums.PaymentMode;
import com.stitch.payment.exception.PaymentException;
import com.stitch.payment.model.entity.Transaction;
import com.stitch.payment.model.dto.PaymentVerificationResponse;
import com.stitch.payment.model.entity.InitializeTransactionRequest;
import com.stitch.payment.model.entity.InitializeTransactionResponse;
import com.stitch.payment.model.enums.TransactionStatus;
import com.stitch.payment.service.PaymentService;
import com.stitch.payment.service.TransactionService;
import com.stitch.payment.util.EnvironmentProperties;
import com.stitch.repository.ProductCartRepository;
import com.stitch.repository.ProductOrderRepository;
import com.stitch.service.ProductOrderService;
import com.stitch.user.model.entity.UserEntity;
import com.stitch.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import static com.stitch.commons.util.SharedUtils.getLoggedInUser;
import com.fasterxml.uuid.Generators;



@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {


    private final ProductOrderService productOrderService;

    private final TransactionService transactionService;

    private final ProductOrderService orderService;

    private final ProductOrderRepository productOrderRepository;

    private final ProductCartRepository productCartRepository;

    private final UserRepository userRepository;
    private final EnvironmentProperties properties;

    public PaymentServiceImpl(ProductOrderService productOrderService, TransactionService transactionService, ProductOrderService orderService, ProductOrderRepository productOrderRepository, ProductCartRepository productCartRepository, UserRepository userRepository, EnvironmentProperties properties) {
        this.productOrderService = productOrderService;
        this.transactionService = transactionService;
        this.orderService = orderService;
        this.productOrderRepository = productOrderRepository;
        this.productCartRepository = productCartRepository;
        this.userRepository = userRepository;
        this.properties = properties;
    }

    private final static TimeBasedGenerator uuidGenerator = Generators.timeBasedGenerator();

    public static String generateUuid() {
        return uuidGenerator.generate().toString();
    }

    public void initializeOrder(InitializeTransactionRequest request) {

        String email = getLoggedInUser()
                .orElseThrow(()-> new PaymentException("Failed to authenticate user", 403));

       UserEntity customer = userRepository.findByEmailAddress(email)
               .orElseThrow(()-> new PaymentException("Customer with Id : " + email + " does not exist", 404));


        List<ProductCart> productCart = productCartRepository.findProductCartByCustomer(customer);
        log.info(" productCarts ---->>>: {}", productCart);

        String transactionId = request.getReference();

        log.info(" transactionId ---->{}", transactionId);

        for (ProductCart productItem : productCart) {
            ProductOrderRequest productOrderRequest = new ProductOrderRequest();
            productOrderRequest.setStatus(OrderStatus.PROCESSING.toString());
            productOrderRequest.setEmailAddress(email);
            productOrderRequest.setTransactionId(generateUuid());
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

            log.info("saved productOrder --->: {}", productOrder);
        }
    }

    @Override
//    @Transactional
    public InitializeTransactionResponse initTransaction(InitializeTransactionRequest request) {

        log.info("Payment initialization request : {}", request);
        try {
            String email = getLoggedInUser()
                    .orElseThrow(() -> new PaymentException("Failed to get authenticated user ", 403));

            StringEntity payStackPayload = createPayStackPayload(request, email);
            HttpResponse response = callPayStackForPayment(payStackPayload);

            log.info("PayStack response : {}", response);
            InitializeTransactionResponse initializeTransactionResponse = mapPaymentResponse(response);
            log.info("initializeTransactionResponse : {}", initializeTransactionResponse);
            initializeOrder(request);
            if (initializeTransactionResponse.isStatus()) {
                log.info("Transaction successfully completed : {}", initializeTransactionResponse);
                paymentVerification(initializeTransactionResponse.getData().getReference());
            }
            return initializeTransactionResponse;
        } catch (UnsupportedEncodingException e) {
            log.info("Deserialization error in paystack initialization : {}", e.getMessage());
            throw new PaymentException("Failed to deserialize", 400);
        } catch (PaymentException e) {
            log.info("Custom exception during paystack initialization :: {}", e.getMessage());
            throw new PaymentException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            log.error("Payment initialization exception : {}", e.getMessage());
            throw new PaymentException(e.getMessage(), HttpStatus.SC_INTERNAL_SERVER_ERROR);

        }

    }

    private String readFromResponse(HttpResponse response) throws IOException {

        if (Objects.isNull(response) || Objects.isNull(response.getEntity())) {
            throw new PaymentException("Failed to get response from paystack", 417);
        }
        int statusCode = response.getStatusLine().getStatusCode();
        StringBuilder result = new StringBuilder();
        if (statusCode == HttpStatus.SC_OK) {
            try (BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                System.out.println("Response from Pay Stack : " + result);
                return result.toString();
            }
        } else {
            throw new PaymentException("Call to PayStack was not successful: Status code : " + statusCode, 417);

        }


    }

    private InitializeTransactionResponse mapPaymentResponse(HttpResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String paymentResponse = readFromResponse(response);
        return mapper.readValue(paymentResponse, InitializeTransactionResponse.class);
    }

    private HttpResponse callPayStackForPayment(StringEntity payStackPayload) {
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(properties.getInitializePaymentUrl());
            post.setEntity(payStackPayload);
            post.setEntity(payStackPayload);
            post.addHeader("Content-type", "application/json");
            post.addHeader("Authorization", "Bearer " + properties.getPaystackSecretKey());
            return client.execute(post);
        }catch (Exception e){
            log.error("POST call to Pay-stack for payment failed : {}", e.getMessage());
            throw new PaymentException("Failed to call Pay-stack for Payment ", 400);
        }
    }

    private HttpResponse callPayStackForVerification(String reference) {
        log.info("reference ===>>: {}", reference);
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(properties.getPaystackVerificationUrl() + reference);
            request.addHeader("Content-type", "application/json");
            request.addHeader("Authorization", "Bearer " + properties.getPaystackSecretKey());
            return client.execute(request);
        }catch (Exception e){
            log.error("GET call to Pay-stack for verification failed : {}", e.getMessage());
            throw new PaymentException("Failed to call Pay-stack for verification ", 400);
        }

    }

    private StringEntity createPayStackPayload(InitializeTransactionRequest request, String email) throws UnsupportedEncodingException {
        log.info("Paystack payload : {}, email:{}", request, email);
        Gson gson = new Gson();
        request.setEmail(email);
        request.setReference(generateUuid());
        request.setChannel(request.getChannel());
        request.setTransaction_charge(2);
        request.setCallback_url(properties.getCallBackURL());
        request.setAmount(request.getAmount().multiply(BigDecimal.valueOf(100)));
        return new StringEntity(gson.toJson(request));
    }

    @Override
//    @Transactional
    public PaymentVerificationResponse paymentVerification(String reference) {
        PaymentVerificationResponse paymentVerificationResponse;

        try {
            HttpResponse response = callPayStackForVerification(reference);

            String payStackResponse = readFromResponse(response);
            ObjectMapper mapper = new ObjectMapper();
            System.out.println("payment verification result  " + payStackResponse);
            paymentVerificationResponse = mapper.readValue(payStackResponse, PaymentVerificationResponse.class);

            if (paymentVerificationResponse == null || !paymentVerificationResponse.getStatus()) {
                throw new PaymentException("pay stack payment verification status is false", 417);
            } else if (paymentVerificationResponse.getMessage().equals("Verification successful")) {

                Transaction transaction = initializeTransaction(paymentVerificationResponse);

                Transaction savedTransaction = transactionService.saveTransaction(transaction);

                log.info("transaction entity saved successfully : {}", savedTransaction);

                List<ProductOrder> productOrders = orderService.getOrdersByTransactionId(savedTransaction.getTransactionId());

                log.info("productOrder entity: {}", productOrders);

                for (ProductOrder productOrder : productOrders) {
                    productOrder.setStatus(OrderStatus.PAYMENT_COMPLETED);
                    ProductOrder p = productOrderRepository.save(productOrder);
                    log.info("product order entity: {}", p);
                }

            }
        } catch (PaymentException e) {
            throw new PaymentException(e.getMessage(), e.getCode());
        } catch (Exception ex) {
            throw new PaymentException(ex.getMessage(), 500);
        }
        return paymentVerificationResponse;
    }

    private Transaction initializeTransaction(PaymentVerificationResponse paymentVerificationResponse) {
        if (Objects.isNull(paymentVerificationResponse) || Objects.isNull(paymentVerificationResponse.getData())) {
            throw new PaymentException("Failed to get transaction details from Paystack ", 417);
        }
        Transaction transaction = Transaction.builder()
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

        if (paymentVerificationResponse.getData().getStatus().equals("abandoned")) {
            System.out.println("ABANDONED HERE");
            transaction.setStatus(TransactionStatus.FAILED);
        }

        if (paymentVerificationResponse.getData().getStatus().equals("success")) {
            System.out.println("SUCCESS HERE");

            transaction.setStatus(TransactionStatus.COMPLETED);
        }
        return transaction;
    }
}