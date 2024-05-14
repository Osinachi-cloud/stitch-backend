package com.stitch.gateway.controller.user;

import com.stitch.commons.exception.StitchException;
import com.stitch.commons.service.CountryService;
import com.stitch.gateway.model.LoginRequest;
import com.stitch.gateway.model.LoginResponse;
import com.stitch.gateway.security.model.Unsecured;
import com.stitch.gateway.security.service.AuthenticationService;
import com.stitch.user.model.dto.*;
import com.stitch.user.service.*;
import com.stitch.wallet.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import com.stitch.user.service.ContactVerificationService;

@Slf4j
@Controller
@CrossOrigin(origins = "http://localhost:4200/vendor-login")
public class VendorController {

    private final VendorService vendorService;
    private final AuthenticationService authenticationService;
    private final ContactVerificationService verificationService;
    private final WalletService walletService;

    private final CountryService countryService;

    public VendorController(VendorService vendorService, AuthenticationService authenticationService, ContactVerificationService verificationService, WalletService walletService, CountryService countryService) {
        this.vendorService = vendorService;
        this.authenticationService = authenticationService;
        this.verificationService = verificationService;
        this.walletService = walletService;
        this.countryService = countryService;
    }

    @Unsecured
    @MutationMapping(value = "createVendor")
    public VendorDto createVendor(@Argument("vendorRequest") VendorRequest vendorRequest) {
        try {
//            String currency = countryService.getCurrencyByCountryName(customerRequest.getCountry());
            VendorDto vendor = vendorService.createVendor(vendorRequest);
//            walletService.createWallet(new WalletRequest(vendor.getVendorId(), vendorRequest.getCurrency(), true));
            return vendor;
        } catch (StitchException exception) {
            throw exception;
        }
        catch (Exception e) {
            log.error("Error creating customer: {}" ,vendorRequest, e);
            throw new StitchException();
        }
    }





//    @Unsecured
//    @MutationMapping(value = "resetPassword")
//    public Response resetPassword(@Argument("passwordResetRequest") PasswordResetRequest passwordResetRequest) {
//        return customerService.resetPassword(passwordResetRequest);
//    }
//
//    @Unsecured
//    @MutationMapping(value = "validateResetCode")
//    public Response validatePasswordResetCode(@Argument("resetCodeValidationRequest") PasswordResetRequest passwordResetRequest) {
//        return customerService.validatePasswordResetCode(passwordResetRequest);
//    }

    @Unsecured
    @MutationMapping(value = "vendorLogin")
    public LoginResponse vendorLogin(@Argument("loginRequest") LoginRequest loginRequest) {
        System.out.println("entered vendor login");
        return authenticationService.authenticateVendor(loginRequest);
    }

}
