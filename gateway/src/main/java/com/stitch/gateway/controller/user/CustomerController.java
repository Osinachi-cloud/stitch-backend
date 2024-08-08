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
import com.stitch.user.service.UserService;
import com.stitch.wallet.model.dto.WalletDto;
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

    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final ContactVerificationService verificationService;
    private final WalletService walletService;

    private final CountryService countryService;

    public CustomerController(UserService userService, AuthenticationService authenticationService, ContactVerificationService verificationService, WalletService walletService, CountryService countryService) {
        this.userService = userService;
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
            CustomerDto customer = userService.createCustomer(customerRequest);
//            walletService.createWallet(new WalletRequest(customer.getCustomerId(), customerRequest.getCurrency(), true));
            return customer;
        }
//        catch (StitchException exception) {
//            throw exception;
//        }
        catch (Exception e) {
            log.error("Error creating customer: {}" ,customerRequest, e);
            throw new StitchException("Error: " + e.getMessage());
        }
    }

    @MutationMapping(value = "updateCustomer")
    public CustomerDto updateCustomer(@Argument("customerRequest") CustomerUpdateRequest customerRequest,
                                      @Argument("emailAddress") String emailAddress) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        try {
            return userService.updateCustomer(customerRequest, emailAddress);
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

            return userService.updateCustomerProfileImage(profileImage, emailAddress);
    }

    @Unsecured
    @MutationMapping(value = "requestPasswordReset")
    public Response requestPasswordReset(@Argument("emailAddress") String emailAddress) {
        return userService.requestPasswordReset(emailAddress);
    }

    @Unsecured
    @MutationMapping(value = "resetPassword")
    public Response resetPassword(@Argument("passwordResetRequest") PasswordResetRequest passwordResetRequest) {
        return userService.resetPassword(passwordResetRequest);
    }

    @Unsecured
    @MutationMapping(value = "validateResetCode")
    public Response validatePasswordResetCode(@Argument("resetCodeValidationRequest") PasswordResetRequest passwordResetRequest) {
        return userService.validatePasswordResetCode(passwordResetRequest);
    }

//    @Unsecured
//    @MutationMapping(value = "vendorLogin")
//    public LoginResponse vendorLogin(@Argument("loginRequest") LoginRequest loginRequest) {
//        return authenticationService.authenticateVendor(loginRequest);
//    }

    @Unsecured
    @MutationMapping(value = "customerLogin")
    public LoginResponse customerLogin(@Argument("loginRequest") LoginRequest loginRequest) {
//        Collection<? extends GrantedAuthority> permission = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
//        System.out.println("testing authorities");
//        System.out.println(permission);
//        System.out.println("testing authorities");
//
//        Collection<SimpleGrantedAuthority> oldAuthorities = (Collection<SimpleGrantedAuthority>)SecurityContextHolder.getContext().getAuthentication().getAuthorities();
//
//        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_VENDOR");
//        List<SimpleGrantedAuthority> updatedAuthorities = new ArrayList<SimpleGrantedAuthority>();
////        updatedAuthorities.remove();
//        updatedAuthorities.add(authority);
//        updatedAuthorities.addAll(oldAuthorities);
//
//        SecurityContextHolder.getContext().setAuthentication(
//                new UsernamePasswordAuthenticationToken(
//                        SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
//                        SecurityContextHolder.getContext().getAuthentication().getCredentials(),
//                        updatedAuthorities));
//
//        Collection<? extends GrantedAuthority> permission2 = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
//
//        System.out.println("testing authorities 2 =====");
//        System.out.println(permission2);
//        System.out.println("testing authorities 2 =====");

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
        return userService.getCustomer(customerId);
    }

    @QueryMapping(value = "customerDetails")
    public CustomerDto getCustomerByEmailAddress(@Argument("emailAddress") String emailAddress) {
        return userService.getCustomerByEmail(emailAddress);
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
        return userService.createPin(user.getUserId(), pin.trim());
    }


    @MutationMapping(value = "resetPinInitiateEmail")
    public Response resetPinInitiateEmail(@Argument("phoneNumber") String phoneNumber) {
        CustomerDto user = authenticationService.getAuthenticatedUser();
        return userService.resetPinInitiateEmail(user.getUserId(), phoneNumber.trim());
    }

    @QueryMapping(value = "verifyResetPinCode")
    public Response verifyResetPinCode(@Argument("code") String code) {
        CustomerDto user = authenticationService.getAuthenticatedUser();
        return userService.verifyResetPinCode(user.getUserId(), code.trim());
    }

    @MutationMapping(value = "resetPin")
    public Response resetPin(@Argument("pin") String pin) {
        CustomerDto user = authenticationService.getAuthenticatedUser();
        return userService.resetPin(user.getUserId(), pin.trim());
    }

    @MutationMapping(value = "allowSaveCard")
    public Response allowSaveCard(@Argument("savedCard") Boolean savedCard) {
        CustomerDto user = authenticationService.getAuthenticatedUser();
        return userService.allowSaveCard(user.getUserId(), savedCard);
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
