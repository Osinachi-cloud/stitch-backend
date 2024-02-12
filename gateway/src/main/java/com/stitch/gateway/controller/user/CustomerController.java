package com.stitch.gateway.controller.user;


import com.stitch.commons.exception.StitchException;
import com.stitch.commons.model.dto.Response;
import com.stitch.commons.service.CountryService;
import com.stitch.gateway.model.LoginRequest;
import com.stitch.gateway.model.LoginResponse;
import com.stitch.gateway.security.model.Token;
import com.stitch.gateway.security.model.Unsecured;
import com.stitch.gateway.security.service.AuthenticationService;
//import com.stitch.notification.model.dto.InAppNotificationResponse;
//import com.stitch.notification.model.dto.InAppNotificationStatsResponse;
import com.stitch.user.model.dto.*;
import com.stitch.user.service.ContactVerificationService;
import com.stitch.user.service.CustomerService;
import com.stitch.wallet.model.dto.WalletDto;
import com.stitch.wallet.model.dto.WalletRequest;
import com.stitch.wallet.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@Slf4j
@Controller
@CrossOrigin(origins = "http://localhost:4200/login")
public class CustomerController {

    private final CustomerService customerService;
    private final AuthenticationService authenticationService;
    private final ContactVerificationService verificationService;
    private final WalletService walletService;

    private final CountryService countryService;

    public CustomerController(CustomerService customerService, AuthenticationService authenticationService, ContactVerificationService verificationService, WalletService walletService, CountryService countryService) {
        this.customerService = customerService;
        this.authenticationService = authenticationService;
        this.verificationService = verificationService;
        this.walletService = walletService;
        this.countryService = countryService;
    }

    @Unsecured
    @MutationMapping(value = "createCustomer")
    public CustomerDto createCustomer(@Argument("customerRequest") CustomerRequest customerRequest) {
        try {
//            String currency = countryService.getCurrencyByCountryName(customerRequest.getCountry());
            CustomerDto customer = customerService.createCustomer(customerRequest);
            walletService.createWallet(new WalletRequest(customer.getCustomerId(), customerRequest.getCurrency(), true));
            return customer;
        } catch (StitchException exception) {
            throw exception;
        }
        catch (Exception e) {
            log.error("Error creating customer: {}" ,customerRequest, e);
            throw new StitchException();
        }
    }

    @MutationMapping(value = "updateCustomer")
    public CustomerDto updateCustomer(@Argument("customerRequest") CustomerUpdateRequest customerRequest,
                                      @Argument("emailAddress") String emailAddress) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        try {
            return customerService.updateCustomer(customerRequest, emailAddress);
        }
        catch (Exception e) {
            log.error("Error updating customer: {}" ,customerRequest, e);
            throw new StitchException();
        }
    }

    @MutationMapping(value = "updateCustomerProfileImage")
    public Response updateCustomerProfileImage(@Argument("profileImage") String profileImage,
                                      @Argument("emailAddress") String emailAddress) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            return customerService.updateCustomerProfileImage(profileImage, emailAddress);
    }


    @Unsecured
    @MutationMapping(value = "requestPasswordReset")
    public Response requestPasswordReset(@Argument("emailAddress") String emailAddress) {
        return customerService.requestPasswordReset(emailAddress);
    }

    @Unsecured
    @MutationMapping(value = "resetPassword")
    public Response resetPassword(@Argument("passwordResetRequest") PasswordResetRequest passwordResetRequest) {
        return customerService.resetPassword(passwordResetRequest);
    }

    @Unsecured
    @MutationMapping(value = "validateResetCode")
    public Response validatePasswordResetCode(@Argument("resetCodeValidationRequest") PasswordResetRequest passwordResetRequest) {
        return customerService.validatePasswordResetCode(passwordResetRequest);
    }


    @Unsecured
    @MutationMapping(value = "login")
    public LoginResponse login(@Argument("loginRequest") LoginRequest loginRequest) {
        return authenticationService.authenticate(loginRequest);
    }

    @Unsecured
    @SchemaMapping(typeName = "LoginResponse")
    public List<WalletDto> wallets(LoginResponse loginResponse) {
        return walletService.getAllWallets(loginResponse.getCustomerId());
    }

    @Unsecured
    @MutationMapping(value = "requestToken")
    public Token requestToken(@Argument("refreshToken") String refreshToken) {
        return authenticationService.refreshAccessToken(refreshToken);
    }


    @QueryMapping(value = "customer")
    public CustomerDto getCustomer(@Argument("customerId") String customerId) {
        return customerService.getCustomer(customerId);
    }

    @QueryMapping(value = "customerDetails")
    public CustomerDto getCustomerByEmailAddress(@Argument("emailAddress") String emailAddress) {
        return customerService.getCustomerByEmail(emailAddress);
    }


    @Unsecured
    @MutationMapping(value = "verifyEmail")
    public VerificationResponse verifyEmail(@Argument("emailAddress") String emailAddress) {
        return verificationService.addEmailAddressForVerification(emailAddress);
    }

    @Unsecured
    @MutationMapping(value = "validateEmailCode")
    public VerificationResponse validateEmailCode(@Argument("verificationRequest") EmailVerificationRequest verificationRequest) {
        return verificationService.verifyEmailAddress(verificationRequest);
    }


    @MutationMapping(value = "createPin")
    public Response createPin(@Argument("pin") String pin) {
        CustomerDto user = authenticationService.getAuthenticatedUser();
        return customerService.createPin(user.getCustomerId(), pin.trim());
    }


    @MutationMapping(value = "resetPinInitiateEmail")
    public Response resetPinInitiateEmail(@Argument("phoneNumber") String phoneNumber) {
        CustomerDto user = authenticationService.getAuthenticatedUser();
        return customerService.resetPinInitiateEmail(user.getCustomerId(), phoneNumber.trim());
    }

    @QueryMapping(value = "verifyResetPinCode")
    public Response verifyResetPinCode(@Argument("code") String code) {
        CustomerDto user = authenticationService.getAuthenticatedUser();
        return customerService.verifyResetPinCode(user.getCustomerId(), code.trim());
    }

    @MutationMapping(value = "resetPin")
    public Response resetPin(@Argument("pin") String pin) {
        CustomerDto user = authenticationService.getAuthenticatedUser();
        return customerService.resetPin(user.getCustomerId(), pin.trim());
    }

    @MutationMapping(value = "allowSaveCard")
    public Response allowSaveCard(@Argument("savedCard") Boolean savedCard) {
        CustomerDto user = authenticationService.getAuthenticatedUser();
        return customerService.allowSaveCard(user.getCustomerId(), savedCard);
    }

//    @MutationMapping(value = "enablePushNotification")
//    public Response enablePushNotification(@Argument("enablePush") Boolean enablePush) {
//        CustomerDto user = authenticationService.getAuthenticatedUser();
//        return customerService.enablePushNotification(user.getCustomerId(), enablePush);
//    }
//
//    @MutationMapping(value = "addPushNotificationToken")
//    public Response addPushNotificationToken(@Argument("pushToken") String pushToken) {
//        CustomerDto user = authenticationService.getAuthenticatedUser();
//        return customerService.addPushNotificationToken(user.getCustomerId(), pushToken);
//    }

//    @QueryMapping(value = "inAppNotificationMessages")
//    public List<InAppNotificationResponse> inAppNotificationMessages(@Argument("inAppRequest") InAppRequest inAppRequest) {
//        CustomerDto user = authenticationService.getAuthenticatedUser();
//        return customerService.customerInAppNotifications(user.getCustomerId(), inAppRequest.getPage(), inAppRequest.getSize());
//    }
//
//    @QueryMapping(value = "inAppNotificationMessagesStats")
//    public InAppNotificationStatsResponse inAppNotificationMessagesStats() {
//        CustomerDto user = authenticationService.getAuthenticatedUser();
//        return customerService.inAppNotificationMessagesStats(user.getCustomerId());
//    }
//
//    @MutationMapping(value = "updateReadInAppMessage")
//    public Response updateReadInAppMessage(@Argument("notificationId") String notificationId) {
//        CustomerDto user = authenticationService.getAuthenticatedUser();
//        return customerService.updateReadInAppNotification(user.getCustomerId(), notificationId);
//    }
}
