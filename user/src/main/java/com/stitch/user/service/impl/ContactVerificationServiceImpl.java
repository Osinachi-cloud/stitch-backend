package com.stitch.user.service.impl;

import com.stitch.commons.enums.ResponseStatus;
import com.stitch.commons.util.NumberUtils;
import com.stitch.user.exception.ContactVerificationException;
import com.stitch.user.model.dto.EmailVerificationRequest;
import com.stitch.user.model.dto.VerificationResponse;
import com.stitch.user.model.entity.ContactVerification;
import com.stitch.user.repository.ContactVerificationRepository;
import com.stitch.user.repository.UserRepository;
import com.stitch.user.service.ContactVerificationService;
import com.stitch.user.util.UserValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
public class ContactVerificationServiceImpl implements ContactVerificationService {

    private final ContactVerificationRepository verificationRepository;
    private final UserRepository customerRepository;

    public ContactVerificationServiceImpl(
            ContactVerificationRepository verificationRepository,
            UserRepository customerRepository
    ) {
        this.verificationRepository = verificationRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public VerificationResponse addEmailAddressForVerification(final String emailAddress) throws ContactVerificationException {
        log.debug("Adding email address [{}] for verification", emailAddress);

        if (!UserValidationUtils.isValidEmail(emailAddress)) {
            log.error("Failed to add email address [{}] due to invalid email address", emailAddress);
            throw new ContactVerificationException(ResponseStatus.INVALID_EMAIL_ADDRESS);
        }

        boolean userExists = customerRepository.existsByEmailAddress(emailAddress);

        if (userExists) {
            log.error("A customer with the email address [{}] already exists", emailAddress);
            throw new ContactVerificationException(ResponseStatus.EMAIL_ADDRESS_EXISTS);
        }

        try {
            final ContactVerification contactVerification = new ContactVerification();
            contactVerification.setEmailAddress(emailAddress);

            final String verificationCode = NumberUtils.generate(5);

            log.debug("Verification code [{}] for email address [{}]", verificationCode, contactVerification.getEmailAddress());

            contactVerification.setVerificationCode(verificationCode);
            contactVerification.setGeneratedOn(Instant.now());

            contactVerification.setExpiredOn(Instant.now().plus(15, ChronoUnit.MINUTES));
            verificationRepository.saveAndFlush(contactVerification);

            VerificationResponse verificationResponse = new VerificationResponse();
            verificationResponse.setCode(0);
            verificationResponse.setMessage("Verification code sent to email");

            log.info("Email address [{}] successfully added for verification", contactVerification.getEmailAddress());
            return verificationResponse;

        } catch (Exception e) {
            log.error("Failed to add email address [{}] for verification", emailAddress, e);
            throw new ContactVerificationException(ResponseStatus.PROCESSING_ERROR);
        }
    }

    @Override
    public VerificationResponse verifyEmailAddress(EmailVerificationRequest verificationRequest) throws ContactVerificationException {

        log.debug("Verifying email address {}", verificationRequest);

        final ContactVerification contactVerification = verificationRepository.findFirstByEmailAddressOrderByDateCreatedDesc(verificationRequest.getEmailAddress());

        if (contactVerification == null) {
            log.error("Email address [{}] not found for verification", verificationRequest.getEmailAddress());
            throw new ContactVerificationException(ResponseStatus.EMAIL_ADDRESS_NOT_FOUND);
        }

        log.debug("Found contact verification: {}", contactVerification);

        if (!contactVerification.getVerificationCode().equals(verificationRequest.getVerificationCode())) {
            log.error("Invalid verification code [{}] for email address {}", verificationRequest.getVerificationCode(), verificationRequest.getEmailAddress());
            throw new ContactVerificationException(ResponseStatus.INVALID_VERIFICATION_CODE);
        }

        if (Instant.now().isAfter(contactVerification.getExpiredOn())) {
            log.error("Verification code [{}] has expired", verificationRequest.getVerificationCode());
            throw new ContactVerificationException(ResponseStatus.EXPIRED_VERIFICATION_CODE);
        }

        try {
            contactVerification.setVerified(true);
            verificationRepository.saveAndFlush(contactVerification);

            VerificationResponse verificationResponse = new VerificationResponse();
            verificationResponse.setCode(0);
            verificationResponse.setMessage("Email verified");

            log.info("Email address [{}] successfully verified", contactVerification.getEmailAddress());
            return verificationResponse;

        } catch (Exception e) {
            log.error("Failed to verify email address [{}]", verificationRequest.getEmailAddress(), e);
            throw new ContactVerificationException(ResponseStatus.PROCESSING_ERROR);
        }
    }
}
