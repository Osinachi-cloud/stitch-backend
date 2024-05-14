package com.stitch.user.service.impl;

import com.stitch.commons.enums.ResponseStatus;
import com.stitch.commons.exception.StitchException;
import com.stitch.commons.model.dto.Response;
import com.stitch.commons.util.NumberUtils;
import com.stitch.commons.util.ResponseUtils;
import com.stitch.user.exception.UserException;
import com.stitch.user.exception.UserNotFoundException;
import com.stitch.user.model.dto.*;
import com.stitch.user.model.entity.ContactVerification;
import com.stitch.user.model.entity.Device;
import com.stitch.user.model.entity.Vendor;
import com.stitch.user.repository.ContactVerificationRepository;
import com.stitch.user.repository.CustomerRepository;
import com.stitch.user.repository.VendorRepository;
import com.stitch.user.service.PasswordService;
import com.stitch.user.service.VendorService;
import com.stitch.user.util.UserValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import static com.stitch.user.util.CountryUtils.getCountryCodeFromEndpoint;
import static com.stitch.user.util.CountryUtils.getCountryNameFromEndpoint;

@Service
@Slf4j
public class VendorServiceImpl implements VendorService {

    private final ContactVerificationRepository verificationRepository;
    private final PasswordService passwordService;

    private final VendorRepository vendorRepository;

    public VendorServiceImpl(ContactVerificationRepository verificationRepository, PasswordService passwordService, VendorRepository vendorRepository) {
        this.verificationRepository = verificationRepository;
        this.passwordService = passwordService;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public VendorDto createVendor(VendorRequest vendorRequest) {

        log.debug("Creating customer with request: {}", vendorRequest);

        String country = getCountryNameFromEndpoint(getCountryCodeFromEndpoint());

        vendorRequest.setCountry(country);

        log.debug("Creating customer with request: {}", vendorRequest);

        validate(vendorRequest);

        Vendor vendor = new Vendor();
        vendor.setVendorId(NumberUtils.generate(9));
        vendor.setFirstName(vendorRequest.getFirstName());
        vendor.setLastName(vendorRequest.getLastName());
        vendor.setEmailAddress(vendorRequest.getEmailAddress());
        vendor.setBusinessName(vendorRequest.getBusinessName());
        vendor.setPhoneNumber(vendorRequest.getPhoneNumber());
        vendor.setCountry(country);
        vendor.setPassword(passwordService.encode(vendorRequest.getPassword()));
        if(Objects.nonNull(vendorRequest.getProfileImage())){
            byte[] imageBytes = Base64.decodeBase64(vendorRequest.getProfileImage());
            String base64EncodedImage = Base64.encodeBase64String(imageBytes);
            vendor.setProfileImage(base64EncodedImage);
        }

        if (vendorRequest.getDevice() != null) {
            vendor.setDevice(new Device(vendorRequest.getDevice()));
        }

        try {

            Vendor newVendor = vendorRepository.save(vendor);

            log.info("Created new customer with ID: {}", newVendor.getVendorId());

            //            notificationService.welcome(new String[]{customer.getEmailAddress()}, customer.getFirstName());

            return new VendorDto(newVendor);
        } catch (Exception e){
            log.error("Error creating customer", e);
            throw new UserException("Failed to create customer", e);
        }

    }

    @Override
    public VendorDto updateVendor(VendorUpdateRequest vendorRequest, String emailAddress) {
        Optional<Vendor> existingVendor = vendorRepository.findByEmailAddress(emailAddress);

        if (existingVendor.isEmpty()) {
            throw new UserException(ResponseStatus.EMAIL_ADDRESS_NOT_FOUND);
        }

        Vendor vendor = existingVendor.get();
        vendor.setFirstName(vendorRequest.getFirstName());
        vendor.setLastName(vendorRequest.getLastName());
        vendor.setCountry(vendorRequest.getCountry());

        try {

            Vendor newVendor = vendorRepository.save(vendor);

            log.info("Updated customer with ID: {}", newVendor.getVendorId());

            return new VendorDto(newVendor);
        } catch (Exception e){
            log.error("Error creating customer", e);
            throw new UserException("Failed to create customer", e);
        }
    }

    @Override
    public Response updateVendorProfileImage(String profileImage, String emailAddress) {
        log.info("email address value: {}", emailAddress);
//        log.info("profileImage: {}", profileImage)
        Optional<Vendor> existingVendor = vendorRepository.findByEmailAddress(emailAddress);

        if (existingVendor.isEmpty()) {
            throw new UserException("customer does not exist");
        }
        log.info("existingCustomer: {}", existingVendor.get().getFirstName());

        Vendor vendor = existingVendor.get();

        if (profileImage != null && !profileImage.isEmpty()) {
            byte[] imageBytes = Base64.decodeBase64(profileImage);
            String base64EncodedImage = Base64.encodeBase64String(imageBytes);
            vendor.setProfileImage(base64EncodedImage);
            Vendor newVendor = vendorRepository.save(vendor);

            log.info("Updated customer with ID: {}", newVendor.getVendorId());

            return ResponseUtils.createSuccessResponse("profile image updated successfully");


        } else {
            throw new StitchException("image field can not be empty");
        }

    }

    @Override
    public VendorDto getVendor(String vendorId) {

        log.debug("Getting customer with ID: {}", vendorId);

        Vendor vendor = getVendorEntity(vendorId);

        VendorDto vendorDto = new VendorDto();
        vendorDto.setVendorId(vendor.getVendorId());
        vendorDto.setFirstName(vendor.getFirstName());
        vendorDto.setLastName(vendor.getLastName());
        vendorDto.setEmailAddress(vendor.getEmailAddress());
        vendorDto.setPhoneNumber(vendor.getPhoneNumber());
        vendorDto.setEnabled(vendor.isEnabled());
//        vendorDto.setHasPin(vendor.getPin() != null);
        vendorDto.setPassword(vendor.getPassword());
        vendorDto.setTier(vendor.getTier() != null ? vendor.getTier().name() : "");
        return vendorDto;
    }

    private Vendor getVendorEntity(String vendorId) {
        return vendorRepository.findByVendorId(vendorId).orElseThrow(() -> new UserNotFoundException(String.format("Vendor [%s] not found", vendorId)));
    }

    @Override
    public VendorDto getVendorByEmail(String emailAddress) {

        log.debug("Getting vendor with email address: {}", emailAddress);
        Vendor vendor = vendorRepository.findByEmailAddress(emailAddress)
                .orElseThrow(() -> new UserNotFoundException(String.format("Vendor [%s] not found", emailAddress)));

        VendorDto vendorDto = new VendorDto();
        vendorDto.setVendorId(vendor.getVendorId());
        vendorDto.setFirstName(vendor.getFirstName());
        vendorDto.setLastName(vendor.getLastName());
        vendorDto.setEmailAddress(vendor.getEmailAddress());
        vendorDto.setPhoneNumber(vendor.getPhoneNumber());
        vendorDto.setCountry(vendor.getCountry());
        vendorDto.setEnabled(vendor.isEnabled());
        vendorDto.setProfileImage(vendor.getProfileImage());
        vendorDto.setAccountLocked(vendor.isAccountLocked());
        vendorDto.setPassword(vendor.getPassword());
        vendorDto.setProfileImage(vendor.getProfileImage());

        if (vendor.getTier() != null) {
            vendorDto.setTier(vendor.getTier().name());
        }
        return vendorDto;
    }

    private void validate(VendorRequest vendorRequest) {
        if (StringUtils.isBlank(vendorRequest.getFirstName()) ||
                StringUtils.isBlank(vendorRequest.getLastName()) ||
                StringUtils.isBlank(vendorRequest.getEmailAddress()) ||
                StringUtils.isBlank(vendorRequest.getPhoneNumber()) ||
                StringUtils.isBlank(vendorRequest.getCountry()) ||
                StringUtils.isBlank(vendorRequest.getPassword())) {
            throw new UserException(ResponseStatus.EMPTY_FIELD_VALUES);
        }

        if (!UserValidationUtils.isValidEmail(vendorRequest.getEmailAddress())) {
            throw new UserException(ResponseStatus.INVALID_EMAIL_ADDRESS);
        }

        if (!UserValidationUtils.isValidPhoneNumber(vendorRequest.getPhoneNumber())) {
            throw new UserException(ResponseStatus.INVALID_PHONE_NUMBER);
        }
        Optional<Vendor> existingVendor = vendorRepository.findByEmailAddress(vendorRequest.getEmailAddress());

        Optional<Vendor> existingCustomerByBusinessName = vendorRepository.findByBusinessName(vendorRequest.getBusinessName());

        if(existingVendor.isPresent()) {
            log.info("vendor ===>>> :{}", existingVendor.get().getEmailAddress());
        }


        if (existingVendor.isPresent()) {
            throw new UserException(ResponseStatus.EMAIL_ADDRESS_EXISTS);
        }

        if (existingCustomerByBusinessName.isPresent()) {
            throw new UserException(ResponseStatus.USERNAME_EXISTS);
        }

        ContactVerification contactVerification = verificationRepository.findFirstByEmailAddressOrderByDateCreatedDesc(vendorRequest.getEmailAddress());

        if (contactVerification == null || !contactVerification.isVerified()) {
            throw new UserException(ResponseStatus.EMAIL_ADDRESS_UNVERIFIED);
        }


        existingVendor = vendorRepository.findByPhoneNumber(vendorRequest.getPhoneNumber());

        if (existingVendor.isPresent()) {
            throw new UserException(ResponseStatus.PHONE_NUMBER_EXISTS);
        }
        passwordService.validateNewPassword(vendorRequest.getPassword());
    }


    @Override
    public void updateLastLogin(VendorDto user) {
        Vendor vendor = getVendorEntity(user.getVendorId());
        vendor.setLastLogin(Instant.now());
        vendor.setLoginAttempts(0);
        vendorRepository.save(vendor);
        log.info("Updated customer [{}] last login", vendor.getVendorId());
    }

    @Override
    public void updateLoginAttempts(String emailAddress) {
        Optional<Vendor> optionalVendor = vendorRepository.findByEmailAddress(emailAddress);
        if (optionalVendor.isEmpty()) {
            log.warn("Customer with email address [{}] does not exist", emailAddress);
            return;
        }
        Vendor vendor = optionalVendor.get();
        int loginAttempts = vendor.getLoginAttempts() + 1;
        vendor.setLoginAttempts(loginAttempts);
        vendorRepository.save(vendor);
        log.info("Updated customer [{}] login attempts to {}", vendor.getEmailAddress(), loginAttempts);

    }
}
