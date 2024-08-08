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
import com.stitch.user.model.dto.*;
import com.stitch.user.model.entity.ContactVerification;
import com.stitch.user.model.entity.UserEntity;
import com.stitch.user.model.entity.Device;
import com.stitch.user.repository.ContactVerificationRepository;
import com.stitch.user.repository.UserRepository;
import com.stitch.user.service.UserService;
import com.stitch.user.service.PasswordService;
import com.stitch.user.service.RoleService;
import com.stitch.user.util.UserValidationUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;

//import static com.stitch.user.util.CountryUtils.getCountryCodeFromEndpoint;
//import static com.stitch.user.util.CountryUtils.getCountryNameFromEndpoint;


@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ContactVerificationRepository verificationRepository;
//    private final NotificationService notificationService;
//    private final PushNotificationService pushNotificationService;
//    private final InAppNotificationService inAppNotificationService;
    private final PasswordService passwordService;

//    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("your-persistence-unit");

    private final RoleService roleService;

    public UserServiceImpl(
            UserRepository userRepository,
            ContactVerificationRepository verificationRepository,
//            NotificationService notificationService,
//            PushNotificationService pushNotificationService,
//            InAppNotificationService inAppNotificationService,
            PasswordService passwordService, RoleService roleService) {
        this.userRepository = userRepository;
        this.verificationRepository = verificationRepository;
//        this.notificationService = notificationService;
//        this.pushNotificationService = pushNotificationService;
//        this.inAppNotificationService = inAppNotificationService;
        this.passwordService = passwordService;
        this.roleService = roleService;
    }

//    void get(){
//        SessionFactory sessionFactory = null; // obtain a SessionFactory instance
//        Session session = sessionFactory.getCurrentSession();
//        AuditReader auditReader = AuditReaderFactory.get(session);
//    }
//
//    public List<UserEntity> getUserEntityRevisions(Long userId) {
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        AuditReader auditReader = AuditReaderFactory.get(entityManager);
//
//        List<UserEntity> revisions = auditReader.createQuery()
//                .forRevisionsOfEntity(UserEntity.class, true, true)
//                .add(AuditEntity.id().eq(userId))
//                .getResultList();
//
//        entityManager.close();
//        return revisions;
//    }
//
//    @Override
//    public void getAudit(){
//        List <UserEntity> userEntities = getUserEntityRevisions(1L);
//      log.info("list : {}", userEntities);
//    }


    @Transactional
    @Override
    public CustomerDto createCustomer(CustomerRequest customerRequest){
        System.out.println(userRepository.findLastChangeRevision(1L));

        log.info("Creating customer with request: {}", customerRequest);

//        String country = getCountryNameFromEndpoint(getCountryCodeFromEndpoint());
        String country = "NIGERIA";
        customerRequest.setCountry(country);

        log.info("Creating customer with request: {}", customerRequest);

        validate(customerRequest);

        UserEntity customer = new UserEntity();
        customer.setUserId(NumberUtils.generate(9));
        customer.setFirstName(customerRequest.getFirstName());
        customer.setLastName(customerRequest.getLastName());
        customer.setEmailAddress(customerRequest.getEmailAddress());
        customer.setUsername(customerRequest.getUsername());
        customer.setPhoneNumber(customerRequest.getPhoneNumber());
        customer.setCountry(country);
        log.info("customer obj 0 : {}", customer);

        log.info("role obj 0 : {}", roleService.findRoleByName(customerRequest.getRoleName()));



        customer.setRole(roleService.findRoleByName(customerRequest.getRoleName()));
        customer.setPassword(passwordService.encode(customerRequest.getPassword()));
        log.info("customer obj 1 : {}", customer);

        if(Objects.nonNull(customerRequest.getProfileImage())){
            log.info("customer obj 2 : {}", customer);

            byte[] imageBytes = Base64.decodeBase64(customerRequest.getProfileImage());
            String base64EncodedImage = Base64.encodeBase64String(imageBytes);
            customer.setProfileImage(base64EncodedImage);
        }

        if (customerRequest.getDevice() != null) {
            log.info("customer obj 3 : {}", customer);

            customer.setDevice(new Device(customerRequest.getDevice()));
        }

        try {
            log.info("customer obj : {}", customer);

            UserEntity newCustomer = userRepository.save(customer);

            log.info("Created new customer with ID: {}", newCustomer.getUserId());

            //            notificationService.welcome(new String[]{customer.getEmailAddress()}, customer.getFirstName());

            return new CustomerDto(newCustomer);
        } catch (Exception e){
            log.error("Error creating customer", e);
            throw new UserException("Failed to create customer", e);
        }
    }

    @Override
    public CustomerDto updateCustomer(CustomerUpdateRequest customerRequest, String emailAddress) {

        Optional<UserEntity> existingCustomer = userRepository.findByEmailAddress(emailAddress);

        if (existingCustomer.isEmpty()) {
            throw new UserException(ResponseStatus.EMAIL_ADDRESS_NOT_FOUND);
        }

        UserEntity customer = existingCustomer.get();
        customer.setFirstName(customerRequest.getFirstName());
        customer.setLastName(customerRequest.getLastName());
        customer.setCountry(customerRequest.getCountry());

        try {

            UserEntity newCustomer = userRepository.saveAndFlush(customer);

            log.info("Updated customer with ID: {}", newCustomer.getUserId());

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
//        log.info("profileImage: {}", profileImage)
            Optional<UserEntity> existingCustomer = userRepository.findByEmailAddress(emailAddress);

            if (existingCustomer.isEmpty()) {
                throw new UserException("customer does not exist");
            }
            log.info("existingCustomer: {}", existingCustomer.get().getFirstName());

            UserEntity customer = existingCustomer.get();

            if (profileImage != null && !profileImage.isEmpty()) {
                byte[] imageBytes = Base64.decodeBase64(profileImage);
                String base64EncodedImage = Base64.encodeBase64String(imageBytes);
                customer.setProfileImage(base64EncodedImage);
                UserEntity newCustomer = userRepository.saveAndFlush(customer);

                log.info("Updated customer with ID: {}", newCustomer.getUserId());

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
        Optional<UserEntity> existingCustomer = userRepository.findByEmailAddress(customerRequest.getEmailAddress());

        Optional<UserEntity> existingCustomerByUsername = userRepository.findByUsername(customerRequest.getUsername());

        if(existingCustomer.isPresent()) {
            log.info("customer ===>>> :{}", existingCustomer.get().getEmailAddress());
        }


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


        existingCustomer = userRepository.findByPhoneNumber(customerRequest.getPhoneNumber());

        if (existingCustomer.isPresent()) {
            throw new UserException(ResponseStatus.PHONE_NUMBER_EXISTS);
        }
        passwordService.validateNewPassword(customerRequest.getPassword());
    }

    @Override
    public CustomerDto getCustomer(String customerId) {

        log.debug("Getting customer with ID: {}", customerId);

        UserEntity customer = getCustomerEntity(customerId);

        CustomerDto customerDto = new CustomerDto();
        customerDto.setUserId(customer.getUserId());
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

    @Override
    public UserEntity getCustomerEntity(String customerId) {
        return userRepository.findByUserId(customerId).orElseThrow(() -> new UserNotFoundException(String.format("Customer [%s] not found", customerId)));
    }

//    @Cacheable(value = "customerCache")
    @Override
    public CustomerDto getCustomerByEmail(String emailAddress) {

        System.out.println("got to login method" + emailAddress);

        log.debug("Getting customer with email address: {}", emailAddress);
        UserEntity customer = userRepository.findByEmailAddress(emailAddress)
                .orElseThrow(() -> new UserNotFoundException(String.format("Customer with this email [%s] not found", emailAddress)));

        CustomerDto customerDto = new CustomerDto();
        customerDto.setUserId(customer.getUserId());
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
        UserEntity customer = getCustomerEntity(user.getUserId());
        customer.setLastLogin(Instant.now());
        customer.setLoginAttempts(0);
        userRepository.saveAndFlush(customer);
        log.info("Updated customer [{}] last login", customer.getUserId());
    }

    @Override
    public void updateLoginAttempts(String emailAddress) {
        Optional<UserEntity> optionalCustomer = userRepository.findByEmailAddress(emailAddress);
        if (optionalCustomer.isEmpty()) {
            log.warn("Customer with email address [{}] does not exist", emailAddress);
            return;
        }
        UserEntity customer = optionalCustomer.get();
        int loginAttempts = customer.getLoginAttempts() + 1;
        customer.setLoginAttempts(loginAttempts);
        userRepository.saveAndFlush(customer);
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
        UserEntity customer = getCustomerEntity(customerId);

        if (customer.getPin() != null) {
            throw new UserException(ResponseStatus.INVALID_PIN_UPDATE);
        }

        if (!NumberUtils.isNumeric(pin) || pin.length() != 4) {
            throw new UserException(ResponseStatus.PIN_FORMAT_ERROR);
        }

        customer.setPin(passwordService.encode(pin));
        userRepository.saveAndFlush(customer);

        return ResponseUtils.createDefaultSuccessResponse();
    }

    @Override
    public Response checkPin(String customerId, String pin) {
        UserEntity customer = getCustomerEntity(customerId);
        int count = customer.getPinAttempts() == null ? 0 : customer.getPinAttempts();

        if (count >= 3) {
            throw new UserException(ResponseStatus.RESET_PIN_FLAG);
        }

        if (!passwordService.passwordMatch(pin, customer.getPin())) {
            customer.setPinAttempts(count + 1);
            userRepository.saveAndFlush(customer);
            throw new UserException(ResponseStatus.WRONG_PIN);
        }

        customer.setPinAttempts(0);
        userRepository.saveAndFlush(customer);

        return ResponseUtils.createDefaultSuccessResponse();
    }


    @Override
    public Response resetPinInitiateEmail(String customerId, String phoneNumber) {

        log.debug("OTP for Reset Pin for customer ID: {}", customerId);

        UserEntity customer = getCustomerEntity(customerId);

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

        UserEntity customer = getCustomerEntity(customerId);

        log.debug("Verifying Reset pin code for customer ID: {}", customer.getUserId());

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
        UserEntity customer = getCustomerEntity(customerId);

        if (!NumberUtils.isNumeric(pin) || pin.length() != 4) {
            throw new UserException(ResponseStatus.PIN_FORMAT_ERROR);
        }

        customer.setPin(passwordService.encode(pin));
        customer.setPinAttempts(0);
        userRepository.saveAndFlush(customer);

        return ResponseUtils.createDefaultSuccessResponse();
    }


    @Override
    public Response allowSaveCard(String customerId, Boolean saveCard) {
        UserEntity customer = getCustomerEntity(customerId);
        customer.setSaveCard(saveCard);
        userRepository.saveAndFlush(customer);
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
