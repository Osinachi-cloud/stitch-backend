package com.stitch.gateway.controller.user;


import com.stitch.commons.exception.StitchException;
import com.stitch.commons.model.dto.PaginatedResponse;
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
            return userService.createCustomer(customerRequest);
        }
        catch (StitchException | InterruptedException e) {
            log.error("Error creating customer: {}" ,customerRequest, e);
            throw new StitchException("Error: " + e.getMessage());
        }
    }

    @Unsecured
    @QueryMapping(value = "getUsers")
    public PaginatedResponse<List<UserDto>> getUsers(@Argument("userFilterRequest") UserFilterRequest request) {
        try {
            return userService.fetchAllUsersBy(request);
        }
        catch (StitchException e) {
            log.error("Error creating customer: {}" ,request, e);
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

    @Unsecured
    @MutationMapping(value = "customerLogin")
    public LoginResponse customerLogin(@Argument("loginRequest") LoginRequest loginRequest) {
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
}
