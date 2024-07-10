package com.stitch.gateway.controller.payment;

import com.stitch.commons.exception.StitchException;
import com.stitch.payment.model.dto.PaymentVerificationResponse;
import com.stitch.payment.model.entity.InitializeTransactionRequest;
import com.stitch.payment.model.entity.InitializeTransactionResponse;
import com.stitch.payment.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;


@Controller
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    @MutationMapping(value = "initializePayment")
    @Transactional
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public InitializeTransactionResponse initializePayment(@Argument("paymentRequest") InitializeTransactionRequest paymentRequest){
        log.info("paymentRequest : {}", paymentRequest);

        try {
            return paymentService.initTransaction(paymentRequest);
        }catch (StitchException e){
            throw new StitchException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @MutationMapping(value = "verifyPayment")
    @Transactional
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public PaymentVerificationResponse verifyPayment(@Argument("paymentReference") String paymentReference){
        try {
            return paymentService.paymentVerification(paymentReference);
        }catch (StitchException e){
            throw new StitchException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

