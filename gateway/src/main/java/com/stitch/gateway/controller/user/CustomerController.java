package com.stitch.gateway.controller.user;


import com.stitch.commons.model.dto.PaginatedResponse;
import com.stitch.commons.model.dto.Response;
import com.stitch.gateway.model.request.EmailRequest;
import com.stitch.gateway.security.model.Unsecured;
import com.stitch.gateway.security.service.AuthenticationService;
import com.stitch.user.model.dto.*;
import com.stitch.user.model.entity.Address;
import com.stitch.user.model.entity.UserEntity;
import com.stitch.user.service.AddressService;
import com.stitch.user.service.ContactVerificationService;
import com.stitch.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.stitch.gateway.util.Constants.BASE_URL;
import static com.stitch.user.util.DtoMapper.mapAddressToDto;
import static org.springframework.http.HttpStatus.CREATED;


@Slf4j
//@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
@RequestMapping(BASE_URL)
public class CustomerController {

    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final ContactVerificationService verificationService;
    private final AddressService addressService;


    @Unsecured
    @PostMapping("/create-customer")
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerRequest customerRequest) {
        return new ResponseEntity<>(userService.createCustomer(customerRequest), CREATED);
    }

    @Unsecured
    @GetMapping("/get-users")
    public ResponseEntity<PaginatedResponse<List<UserDto>>> getUsers(
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
         @RequestParam(required = false) String firstName, @RequestParam(required = false) String lastName,
         @RequestParam(required = false) String email, @RequestParam(required = false) Long roleId) {
            return ResponseEntity.ok(userService.fetchAllUsersBy(page, size, firstName, lastName, email, roleId));

    }

    @PutMapping("/update-customer")
    public ResponseEntity<CustomerDto> updateCustomer(@RequestBody CustomerUpdateRequest customerRequest,
                                      @RequestParam("emailAddress") String emailAddress) {
            return ResponseEntity.ok(userService.updateCustomer(customerRequest, emailAddress));

    }

    @PostMapping(value = "/update-customer-profile-image")
    public ResponseEntity<Response> updateCustomerProfileImage(@RequestParam("profileImage") String profileImage,
                                               @RequestParam("emailAddress") String emailAddress) {

        return ResponseEntity.ok(userService.updateCustomerProfileImage(profileImage, emailAddress));
    }

    @Unsecured
    @PostMapping("/request-password-reset")
    public ResponseEntity<Response> requestPasswordReset(@RequestBody EmailRequest emailAddress) {
        return ResponseEntity.ok(userService.requestPasswordReset(emailAddress.getEmail()));
    }

    @Unsecured
    @PostMapping("/reset-password")
    public ResponseEntity<Response> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) {
        return ResponseEntity.ok(userService.resetPassword(passwordResetRequest));
    }

    @Unsecured
    @PostMapping("/change-password")
    public ResponseEntity<Response> changePassword(@RequestBody ChangePasswordRequest passwordChangeRequest) {
        return ResponseEntity.ok(userService.changePassword(passwordChangeRequest));
    }

    @Unsecured
    @PostMapping(value = "/validate-reset-code")
    public ResponseEntity<Response> validatePasswordResetCode(@RequestBody PasswordResetRequest passwordResetRequest) {
        return ResponseEntity.ok(userService.validatePasswordResetCode(passwordResetRequest));
    }

    @GetMapping(value = "/customer")
    public ResponseEntity<CustomerDto> getCustomer(@RequestParam("customerId") String customerId) {
        return ResponseEntity.ok(userService.getCustomer(customerId));
    }

    @GetMapping("/customer-details")
    public ResponseEntity<CustomerDto> getCustomerByEmailAddress(@RequestParam("emailAddress") String emailAddress) {
        return ResponseEntity.ok(userService.getCustomerByEmail(emailAddress));
    }


    @Unsecured
    @PostMapping(value = "/verify-email")
    public ResponseEntity<VerificationResponse> verifyEmail(@RequestBody EmailRequest contactVerification) {
        log.info("contactVerification ===>>>: {}", contactVerification);
        return ResponseEntity.ok(verificationService.addEmailAddressForVerification(contactVerification.getEmail()));
    }

    @Unsecured
    @PostMapping(value = "/validateEmailCode")
    public VerificationResponse validateEmailCode(@RequestBody EmailVerificationRequest verificationRequest) {
        return verificationService.verifyEmailAddress(verificationRequest);
    }

    @PostMapping(value = "allowSaveCard")
    public Response allowSaveCard(@RequestParam("savedCard") Boolean savedCard) {
        CustomerDto user = authenticationService.getAuthenticatedUser();
        return userService.allowSaveCard(user.getUserId(), savedCard);
    }

    @Unsecured
    @PostMapping("/addresses")
    public ResponseEntity<AddressDto> getAddresses(@RequestBody AddressDto addressRequest) {
        System.out.println("11111111111111111111111111111" + addressRequest);
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userService.getCustomerEmail(userName);
         Address address = addressService.createAddress(addressRequest);
         user.setAddress(address);
         UserEntity savedUser = userService.saveUserEntity(user);
         log.info("saved user ==================> : {}", savedUser);
       return ResponseEntity.ok(mapAddressToDto(address));
    }

    @Unsecured
    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDto>> getAddress() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userService.getCustomerEmail(userName);
        log.info("Get User Address ==================> : {}", user.getAddress());
        List<AddressDto> addressDtoList = new ArrayList<>();
        addressDtoList.add(mapAddressToDto(user.getAddress()));
        return ResponseEntity.ok(addressDtoList);
    }
}

