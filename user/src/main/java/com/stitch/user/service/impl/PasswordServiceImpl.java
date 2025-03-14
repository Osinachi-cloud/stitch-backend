package com.stitch.user.service.impl;

import com.stitch.commons.enums.ResponseStatus;
import com.stitch.commons.model.dto.Response;
import com.stitch.commons.util.NumberUtils;
import com.stitch.commons.util.ResponseUtils;
//import com.stitch.notification.service.NotificationService;
import com.stitch.user.exception.PasswordException;
import com.stitch.user.exception.UserNotFoundException;
import com.stitch.user.model.dto.PasswordResetRequest;
import com.stitch.user.model.entity.UserEntity;
import com.stitch.user.model.entity.PasswordReset;
import com.stitch.user.repository.UserRepository;
import com.stitch.user.repository.PasswordResetRepository;
import com.stitch.user.service.PasswordService;
import com.stitch.user.util.UserValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PasswordServiceImpl implements PasswordService {

    private final UserRepository customerRepository;
    private final PasswordResetRepository passwordResetRepository;
    private final PasswordEncoder passwordEncoder;


    public PasswordServiceImpl(
            UserRepository customerRepository,
            PasswordResetRepository passwordResetRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.customerRepository = customerRepository;
        this.passwordResetRepository = passwordResetRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public Response requestPasswordReset(String emailAddress) {

        log.debug("Request for password reset from customer with email address [{}]", emailAddress);

        if (!UserValidationUtils.isValidEmail(emailAddress)) {
            log.warn("Customer email address [{}] invalid for password reset", emailAddress);
            throw new PasswordException(ResponseStatus.INVALID_EMAIL_ADDRESS);
        }

        Optional<UserEntity> optionalCustomer = customerRepository.findByEmailAddress(emailAddress);

        if (optionalCustomer.isEmpty()) {
            log.error("A customer with email address [{}] not found for password reset", emailAddress);
            throw new PasswordException(ResponseStatus.USER_NOT_FOUND);
        }
        final UserEntity customer = optionalCustomer.get();

        try {
            PasswordReset passwordReset = new PasswordReset();
            passwordReset.setEmailAddress(emailAddress);

            String resetCode = NumberUtils.generate(5);

            passwordReset.setResetCode(resetCode);
            passwordReset.setGeneratedOn(Instant.now());

            passwordReset.setExpiredOn(Instant.now().plus(15, ChronoUnit.MINUTES));
            passwordResetRepository.saveAndFlush(passwordReset);

            log.info("Customer [{}] has requested a password reset process and reset code sent to email [{}]", customer.getUserId(), customer.getEmailAddress());
            return ResponseUtils.createSuccessResponse("Password reset code sent to email");

        } catch (Exception e) {
            log.error("Failed to request password reset for email address [{}]", emailAddress, e);
            throw new PasswordException(ResponseStatus.PROCESSING_ERROR);
        }
    }

    @Override
    public Response resetPassword(PasswordResetRequest passwordResetRequest) {

        log.debug("Resetting password for customer with email address {}", passwordResetRequest.getEmailAddress());

        final PasswordReset passwordReset = passwordResetRepository.findFirstByEmailAddressOrderByDateCreatedDesc(passwordResetRequest.getEmailAddress());

        if (passwordReset == null) {
            log.error("Email address [{}] not found for password reset", passwordResetRequest.getEmailAddress());
            throw new PasswordException(ResponseStatus.EMAIL_ADDRESS_NOT_FOUND);
        }

        log.debug("Found password reset : {}", passwordReset);

        if (!passwordReset.isVerified()){
            throw new PasswordException(ResponseStatus.PASSWORD_RESET_CODE_UNVERIFIED);
        }

        if (!passwordReset.getResetCode().equals(passwordResetRequest.getResetCode())) {
            log.error("Invalid password reset code [{}] for email address {}", passwordResetRequest.getResetCode(), passwordResetRequest.getEmailAddress());
            throw new PasswordException(ResponseStatus.INVALID_RESET_CODE);
        }

        if (Instant.now().isAfter(passwordReset.getExpiredOn())) {
            log.error("Password reset code [{}] has expired", passwordResetRequest.getResetCode());
            throw new PasswordException(ResponseStatus.EXPIRED_RESET_CODE);
        }

        validateNewPassword(passwordResetRequest);

        try {

            UserEntity customer = customerRepository.findByEmailAddress(passwordReset.getEmailAddress())
                    .orElseThrow(() -> new UserNotFoundException(ResponseStatus.USER_NOT_FOUND));

            customer.setPassword(encode(passwordResetRequest.getNewPassword()));
            customer.setLastPasswordChange(Instant.now());
            customerRepository.save(customer);

            passwordReset.setVerified(true);
            passwordResetRepository.save(passwordReset);

            log.info("Customer [{}] password reset successfully", customer.getUserId());
            return ResponseUtils.createDefaultSuccessResponse();

        } catch (Exception e) {
            log.error("Failed to reset password for customer with email address [{}]", passwordResetRequest.getEmailAddress(), e);
            throw new PasswordException(ResponseStatus.PROCESSING_ERROR);
        }
    }

    @Override
    public String encode(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public void validateNewPassword(PasswordResetRequest passwordResetRequest) {

        if (StringUtils.isBlank(passwordResetRequest.getNewPassword()) || StringUtils.isBlank(passwordResetRequest.getConfirmPassword())) {
            throw new PasswordException(ResponseStatus.PASSWORD_EMPTY);
        }

        List<String> passwordErrors = UserValidationUtils.getPasswordErrors(passwordResetRequest.getNewPassword());

        if (!passwordErrors.isEmpty()) {
            throw new PasswordException(passwordErrors.toString());
        }

        if (!passwordResetRequest.getNewPassword().equals(passwordResetRequest.getConfirmPassword())) {
            throw new PasswordException(ResponseStatus.PASSWORD_MISMATCH);
        }
    }

    @Override
    public void validateNewPassword(String password) {

        if (StringUtils.isBlank(password)) {
            throw new PasswordException(ResponseStatus.PASSWORD_EMPTY);
        }
        List<String> passwordErrors = UserValidationUtils.getPasswordErrors(password);

        if (!passwordErrors.isEmpty()) {
            throw new PasswordException(passwordErrors.toString());
        }
    }

    @Override
    public Response validatePasswordResetCode(PasswordResetRequest passwordResetRequest) {

        log.debug("Validating password reset code for customer with email address {}", passwordResetRequest.getEmailAddress());

        final PasswordReset passwordReset = passwordResetRepository.findFirstByEmailAddressOrderByDateCreatedDesc(passwordResetRequest.getEmailAddress());

        if (passwordReset == null) {
            log.error("Email address [{}] not found for password reset", passwordResetRequest.getEmailAddress());
            throw new PasswordException(ResponseStatus.EMAIL_ADDRESS_NOT_FOUND);
        }

        log.debug("Found password reset : {}", passwordReset);

        if (!passwordReset.getResetCode().equals(passwordResetRequest.getResetCode())) {
            log.error("Invalid password reset code [{}] for email address [{}]", passwordResetRequest.getResetCode(), passwordResetRequest.getEmailAddress());
            throw new PasswordException(ResponseStatus.INVALID_RESET_CODE);
        }

        if (Instant.now().isAfter(passwordReset.getExpiredOn())) {
            log.error("Password reset code [{}] has expired", passwordResetRequest.getResetCode());
            throw new PasswordException(ResponseStatus.EXPIRED_RESET_CODE);
        }


        try {
            passwordReset.setVerified(true);
            passwordResetRepository.saveAndFlush(passwordReset);

            log.info("Password reset code [{}] successfully verified", passwordReset.getEmailAddress());
            return ResponseUtils.createSuccessResponse("Successful");

        } catch (Exception e) {
            log.error("Failed to validate password reset code sent to email [{}]", passwordReset.getEmailAddress(), e);
            throw new PasswordException(ResponseStatus.PROCESSING_ERROR);
        }
    }

    @Override
    public boolean passwordMatch(String rawPassword, String encrypted) {
        return passwordEncoder.matches(rawPassword, encrypted);
    }
}
