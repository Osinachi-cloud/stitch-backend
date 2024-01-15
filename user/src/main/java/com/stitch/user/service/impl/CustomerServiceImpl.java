package com.stitch.user.service.impl;

import com.stitch.commons.enums.ResponseStatus;
import com.stitch.commons.exception.StitchException;
import com.stitch.commons.model.dto.Response;
import com.stitch.commons.util.NumberUtils;
import com.stitch.commons.util.ResponseUtils;
//import com.stitch.notification.model.dto.InAppNotificationResponse;
//import com.stitch.notification.model.dto.InAppNotificationStatsResponse;
//import com.stitch.notification.service.InAppNotificationService;
//import com.stitch.notification.service.NotificationService;
//import com.stitch.notification.service.PushNotificationService;
import com.stitch.user.exception.ContactVerificationException;
import com.stitch.user.exception.UserException;
import com.stitch.user.exception.UserNotFoundException;
import com.stitch.user.model.dto.CustomerDto;
import com.stitch.user.model.dto.CustomerRequest;
import com.stitch.user.model.dto.CustomerUpdateRequest;
import com.stitch.user.model.dto.PasswordResetRequest;
import com.stitch.user.model.entity.ContactVerification;
import com.stitch.user.model.entity.Customer;
import com.stitch.user.model.entity.Device;
import com.stitch.user.repository.ContactVerificationRepository;
import com.stitch.user.repository.CustomerRepository;
import com.stitch.user.service.CustomerService;
import com.stitch.user.service.PasswordService;
import com.stitch.user.util.UserValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;


@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ContactVerificationRepository verificationRepository;
//    private final NotificationService notificationService;
//    private final PushNotificationService pushNotificationService;
//    private final InAppNotificationService inAppNotificationService;
    private final PasswordService passwordService;

    public CustomerServiceImpl(
            CustomerRepository customerRepository,
            ContactVerificationRepository verificationRepository,
//            NotificationService notificationService,
//            PushNotificationService pushNotificationService,
//            InAppNotificationService inAppNotificationService,
            PasswordService passwordService) {
        this.customerRepository = customerRepository;
        this.verificationRepository = verificationRepository;
//        this.notificationService = notificationService;
//        this.pushNotificationService = pushNotificationService;
//        this.inAppNotificationService = inAppNotificationService;
        this.passwordService = passwordService;
    }


    @Transactional
    @Override
    public CustomerDto createCustomer(CustomerRequest customerRequest) {

        log.debug("Creating customer with request: {}", customerRequest);

        validate(customerRequest);

        Customer customer = new Customer();
        customer.setCustomerId(NumberUtils.generate(9));
        customer.setFirstName(customerRequest.getFirstName());
        customer.setLastName(customerRequest.getLastName());
        customer.setEmailAddress(customerRequest.getEmailAddress());
        customer.setUsername(customerRequest.getUsername());
        customer.setPhoneNumber(customerRequest.getPhoneNumber());
        customer.setCountry(customerRequest.getCountry());
        customer.setPassword(passwordService.encode(customerRequest.getPassword()));
        byte[] imageBytes = Base64.decodeBase64(customerRequest.getProfileImage());
        String base64EncodedImage = Base64.encodeBase64String(imageBytes);
        customer.setProfileImage(base64EncodedImage);
        if (customerRequest.getDevice() != null) {
            customer.setDevice(new Device(customerRequest.getDevice()));
        }

        try {

            Customer newCustomer = customerRepository.saveAndFlush(customer);

            log.info("Created new customer with ID: {}", newCustomer.getCustomerId());

            //            notificationService.welcome(new String[]{customer.getEmailAddress()}, customer.getFirstName());

            return new CustomerDto(newCustomer);
        } catch (Exception e){
            log.error("Error creating customer", e);
            throw new UserException("Failed to create customer", e);
        }
    }

    @Override
    public CustomerDto updateCustomer(CustomerUpdateRequest customerRequest, String emailAddress) {

        Optional<Customer> existingCustomer = customerRepository.findByEmailAddress(emailAddress);

        if (existingCustomer.isEmpty()) {
            throw new UserException(ResponseStatus.EMAIL_ADDRESS_NOT_FOUND);
        }

        Customer customer = existingCustomer.get();
        customer.setFirstName(customerRequest.getFirstName());
        customer.setLastName(customerRequest.getLastName());
        customer.setCountry(customerRequest.getCountry());

        try {

            Customer newCustomer = customerRepository.saveAndFlush(customer);

            log.info("Updated customer with ID: {}", newCustomer.getCustomerId());

            return new CustomerDto(newCustomer);
        } catch (Exception e){
            log.error("Error creating customer", e);
            throw new UserException("Failed to create customer", e);
        }

    }

//    @Override
//    public Response updateCustomerProfileImage(String profileImage, String emailAddress) {
//        log.info("email address value: {}", emailAddress);
//
//        log.info("profileImage: {}", profileImage);
//
//        Optional<Customer> existingCustomer = customerRepository.findByEmailAddress(emailAddress);
//
//        if (existingCustomer.isEmpty()) {
//            throw new StitchException("customer does not exist");
//        }
//        log.info("existingCustomer: {}", existingCustomer.get().getFirstName());
//
//
//        Customer customer = existingCustomer.get();
//
//        if(profileImage != null && !profileImage.isEmpty()){
//            byte[] imageBytes = Base64.decodeBase64(profileImage);
//            String base64EncodedImage = Base64.encodeBase64String(imageBytes);
//            customer.setProfileImage(base64EncodedImage);
//        }else {
//            throw new StitchException("image field can not be empty");
//        }
//
//        try {
//
//            Customer newCustomer = customerRepository.saveAndFlush(customer);
//
//            log.info("Updated customer with ID: {}", newCustomer.getCustomerId());
//
//            return ResponseUtils.createSuccessResponse("profile image updated successfully");
//        } catch (Exception e){
//            log.error("Error creating customer", e);
//            throw new UserException("Failed to create customer", e);
//        }
//
//    }


    @Override
    public Response updateCustomerProfileImage(String profileImage, String emailAddress) {
        log.info("email address value: {}", emailAddress);
        log.info("profileImage: {}", profileImage);

            Optional<Customer> existingCustomer = customerRepository.findByEmailAddress(emailAddress);

            if (existingCustomer.isEmpty()) {
                throw new UserException("customer does not exist");
            }
            log.info("existingCustomer: {}", existingCustomer.get().getFirstName());

            Customer customer = existingCustomer.get();

            if (profileImage != null && !profileImage.isEmpty()) {
                byte[] imageBytes = Base64.decodeBase64(profileImage);
                String base64EncodedImage = Base64.encodeBase64String(imageBytes);
                customer.setProfileImage(base64EncodedImage);
                Customer newCustomer = customerRepository.saveAndFlush(customer);

                log.info("Updated customer with ID: {}", newCustomer.getCustomerId());

                return ResponseUtils.createSuccessResponse("profile image updated successfully");


            } else {
                throw new StitchException("image field can not be empty");
            }
    }



    private void validate(CustomerRequest customerRequest) {
        if (StringUtils.isBlank(customerRequest.getFirstName()) ||
                StringUtils.isBlank(customerRequest.getLastName()) ||
                StringUtils.isBlank(customerRequest.getEmailAddress()) ||
                StringUtils.isBlank(customerRequest.getPhoneNumber()) ||
                StringUtils.isBlank(customerRequest.getCountry()) ||
                StringUtils.isBlank(customerRequest.getPassword())) {
            throw new UserException(ResponseStatus.EMPTY_FIELD_VALUES);
        }

        if (!UserValidationUtils.isValidEmail(customerRequest.getEmailAddress())) {
            throw new UserException(ResponseStatus.INVALID_EMAIL_ADDRESS);
        }

        if (!UserValidationUtils.isValidPhoneNumber(customerRequest.getPhoneNumber())) {
            throw new UserException(ResponseStatus.INVALID_PHONE_NUMBER);
        }
        Optional<Customer> existingCustomer = customerRepository.findByEmailAddress(customerRequest.getEmailAddress());

        Optional<Customer> existingCustomerByUsername = customerRepository.findByUsername(customerRequest.getUsername());


        if (existingCustomer.isPresent()) {
            throw new UserException(ResponseStatus.EMAIL_ADDRESS_EXISTS);
        }

        if (existingCustomerByUsername.isPresent()) {
            throw new UserException(ResponseStatus.USERNAME_EXISTS);
        }

        ContactVerification contactVerification = verificationRepository.findFirstByEmailAddressOrderByDateCreatedDesc(customerRequest.getEmailAddress());

        if (contactVerification == null || !contactVerification.isVerified()) {
            throw new UserException(ResponseStatus.EMAIL_ADDRESS_UNVERIFIED);
        }


        existingCustomer = customerRepository.findByPhoneNumber(customerRequest.getPhoneNumber());

        if (existingCustomer.isPresent()) {
            throw new UserException(ResponseStatus.PHONE_NUMBER_EXISTS);
        }
        passwordService.validateNewPassword(customerRequest.getPassword());
    }

    @Override
    public CustomerDto getCustomer(String customerId) {

        log.debug("Getting customer with ID: {}", customerId);

        Customer customer = getCustomerEntity(customerId);

        CustomerDto customerDto = new CustomerDto();
        customerDto.setCustomerId(customer.getCustomerId());
        customerDto.setFirstName(customer.getFirstName());
        customerDto.setLastName(customer.getLastName());
        customerDto.setEmailAddress(customer.getEmailAddress());
        customerDto.setPhoneNumber(customer.getPhoneNumber());
        customerDto.setEnabled(customer.isEnabled());
        customerDto.setHasPin(customer.getPin() != null);
        customerDto.setPassword(customer.getPassword());
        customerDto.setTier(customer.getTier() != null ? customer.getTier().name() : "");
        return customerDto;
    }

    private Customer getCustomerEntity(String customerId) {
        return customerRepository.findByCustomerId(customerId).orElseThrow(() -> new UserNotFoundException(String.format("Customer [%s] not found", customerId)));
    }

    @Cacheable(value = "customerCache")
    @Override
    public CustomerDto getCustomerByEmail(String emailAddress) {

        log.debug("Getting customer with email address: {}", emailAddress);
        Customer customer = customerRepository.findByEmailAddress(emailAddress)
                .orElseThrow(() -> new UserNotFoundException(String.format("Customer [%s] not found", emailAddress)));

        CustomerDto customerDto = new CustomerDto();
        customerDto.setCustomerId(customer.getCustomerId());
        customerDto.setFirstName(customer.getFirstName());
        customerDto.setLastName(customer.getLastName());
        customerDto.setEmailAddress(customer.getEmailAddress());
        customerDto.setPhoneNumber(customer.getPhoneNumber());
        customerDto.setCountry(customer.getCountry());
        customerDto.setEnabled(customer.isEnabled());
        customerDto.setProfileImage(customer.getProfileImage());
        customerDto.setAccountLocked(customer.isAccountLocked());
        customerDto.setPassword(customer.getPassword());
        customerDto.setProfileImage(customer.getProfileImage());
        customerDto.setSaveCard(customer.isSaveCard());
        customerDto.setEnablePush(customer.isEnablePush());
        customerDto.setHasPin(customer.getPin() != null);

        if (customer.getTier() != null) {
            customerDto.setTier(customer.getTier().name());
        }
        return customerDto;
    }

    @Override
    public void updateLastLogin(CustomerDto user) {
        Customer customer = getCustomerEntity(user.getCustomerId());
        customer.setLastLogin(Instant.now());
        customer.setLoginAttempts(0);
        customerRepository.saveAndFlush(customer);
        log.info("Updated customer [{}] last login", customer.getCustomerId());
    }

    @Override
    public void updateLoginAttempts(String emailAddress) {
        Optional<Customer> optionalCustomer = customerRepository.findByEmailAddress(emailAddress);
        if (optionalCustomer.isEmpty()) {
            log.warn("Customer with email address [{}] does not exist", emailAddress);
            return;
        }
        Customer customer = optionalCustomer.get();
        int loginAttempts = customer.getLoginAttempts() + 1;
        customer.setLoginAttempts(loginAttempts);
        customerRepository.saveAndFlush(customer);
        log.info("Updated customer [{}] login attempts to {}", customer.getEmailAddress(), loginAttempts);

    }

    @Override
    public Response requestPasswordReset(String emailAddress) {
        return passwordService.requestPasswordReset(emailAddress);
    }

    @Override
    public Response resetPassword(PasswordResetRequest passwordResetRequest) {
        return passwordService.resetPassword(passwordResetRequest);
    }

    @Override
    public Response validatePasswordResetCode(PasswordResetRequest passwordResetRequest) {
        return passwordService.validatePasswordResetCode(passwordResetRequest);

    }

    @Override
    public Response createPin(String customerId, String pin) {
        Customer customer = getCustomerEntity(customerId);

        if (customer.getPin() != null) {
            throw new UserException(ResponseStatus.INVALID_PIN_UPDATE);
        }

        if (!NumberUtils.isNumeric(pin) || pin.length() != 4) {
            throw new UserException(ResponseStatus.PIN_FORMAT_ERROR);
        }

        customer.setPin(passwordService.encode(pin));
        customerRepository.saveAndFlush(customer);

        return ResponseUtils.createDefaultSuccessResponse();
    }

    @Override
    public Response checkPin(String customerId, String pin) {
        Customer customer = getCustomerEntity(customerId);
        int count = customer.getPinAttempts() == null ? 0 : customer.getPinAttempts();

        if (count >= 3) {
            throw new UserException(ResponseStatus.RESET_PIN_FLAG);
        }

        if (!passwordService.passwordMatch(pin, customer.getPin())) {
            customer.setPinAttempts(count + 1);
            customerRepository.saveAndFlush(customer);
            throw new UserException(ResponseStatus.WRONG_PIN);
        }

        customer.setPinAttempts(0);
        customerRepository.saveAndFlush(customer);

        return ResponseUtils.createDefaultSuccessResponse();
    }


    @Override
    public Response resetPinInitiateEmail(String customerId, String phoneNumber) {

        log.debug("OTP for Reset Pin for customer ID: {}", customerId);

        Customer customer = getCustomerEntity(customerId);

        String customerPhoneNumber = customer.getPhoneNumber();

        if (!customerPhoneNumber.substring(customerPhoneNumber.length() - 4).equals(phoneNumber)) {
            throw new UserException(ResponseStatus.PHONE_NUMBER_NOT_FOUND);
        }

        try {
            final ContactVerification contactVerification = new ContactVerification();
            contactVerification.setEmailAddress(customer.getEmailAddress());

            final String verificationCode = NumberUtils.generate(5);

            log.debug("Reset code [{}] for email address [{}]", verificationCode, contactVerification.getEmailAddress());

            contactVerification.setVerificationCode(verificationCode);
            contactVerification.setGeneratedOn(Instant.now());

            contactVerification.setExpiredOn(Instant.now().plus(15, ChronoUnit.MINUTES));
            verificationRepository.saveAndFlush(contactVerification);

//            notificationService.resetPin(new String[]{contactVerification.getEmailAddress()}, verificationCode, customer.getFirstName());

            log.info("Email address [{}] successfully added for verification", contactVerification.getEmailAddress());
        } catch (Exception e) {
            log.error("Failed to send email to [{}] for reset pin", customer.getEmailAddress(), e);
            throw new ContactVerificationException(ResponseStatus.PROCESSING_ERROR);
        }
        return ResponseUtils.createDefaultSuccessResponse();
    }

    public Response verifyResetPinCode(String customerId, String code) {

        Customer customer = getCustomerEntity(customerId);

        log.debug("Verifying Reset pin code for customer ID: {}", customer.getCustomerId());

        final ContactVerification contactVerification = verificationRepository
                .findFirstByEmailAddressOrderByDateCreatedDesc(customer.getEmailAddress());

        if (contactVerification == null) {
            log.error("Email address [{}] not found for verification", customer.getEmailAddress());
            throw new ContactVerificationException(ResponseStatus.EMAIL_ADDRESS_NOT_FOUND);
        }

        log.debug("Found contact verification: {}", contactVerification);

        if (!contactVerification.getVerificationCode().equals(code)) {
            log.error("Invalid verification code [{}] for email address {}", code, customer.getEmailAddress());
            throw new ContactVerificationException(ResponseStatus.INVALID_VERIFICATION_CODE);
        }

        if (Instant.now().isAfter(contactVerification.getExpiredOn())) {
            log.error("Verification code [{}] has expired", code);
            throw new ContactVerificationException(ResponseStatus.EXPIRED_VERIFICATION_CODE);
        }

        contactVerification.setVerified(true);
        verificationRepository.saveAndFlush(contactVerification);

        return ResponseUtils.createDefaultSuccessResponse();
    }

    @Override
    public Response resetPin(String customerId, String pin) {
        Customer customer = getCustomerEntity(customerId);

        if (!NumberUtils.isNumeric(pin) || pin.length() != 4) {
            throw new UserException(ResponseStatus.PIN_FORMAT_ERROR);
        }

        customer.setPin(passwordService.encode(pin));
        customer.setPinAttempts(0);
        customerRepository.saveAndFlush(customer);

        return ResponseUtils.createDefaultSuccessResponse();
    }


    @Override
    public Response allowSaveCard(String customerId, Boolean saveCard) {
        Customer customer = getCustomerEntity(customerId);
        customer.setSaveCard(saveCard);
        customerRepository.saveAndFlush(customer);
        return ResponseUtils.createDefaultSuccessResponse();
    }

//    @Override
//    public Response enablePushNotification(String customerId, Boolean enablePush) {
//        Customer customer = getCustomerEntity(customerId);
//        customer.setEnablePush(enablePush);
//        customerRepository.saveAndFlush(customer);
//        return ResponseUtils.createDefaultSuccessResponse();
//    }
//
//    @Override
//    public Response addPushNotificationToken(String customerId, String pushToken) {
//        pushNotificationService.saveCustomerToken(customerId, pushToken);
//        return ResponseUtils.createDefaultSuccessResponse();
//    }

//    @Override
//    public List<InAppNotificationResponse> customerInAppNotifications(String customerId, int page, int size) {
//        return inAppNotificationService.fetchAllCustomerNotification(customerId, page, size);
//    }
//
//    @Override
//    public Response updateReadInAppNotification(String customerId, String notificationId) {
//        inAppNotificationService.updateReadInAppNotification(notificationId, customerId);
//        return ResponseUtils.createDefaultSuccessResponse();
//    }
//
//    @Override
//    public InAppNotificationStatsResponse inAppNotificationMessagesStats(String customerId) {
//        return inAppNotificationService.customerInAppNotificationStats(customerId);
//    }
}
