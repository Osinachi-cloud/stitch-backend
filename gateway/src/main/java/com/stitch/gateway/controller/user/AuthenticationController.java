package com.stitch.gateway.controller.user;

import com.stitch.commons.model.dto.Response;
import com.stitch.gateway.model.request.LoginRequest;
import com.stitch.gateway.model.response.LoginResponse;
import com.stitch.gateway.security.model.Token;
import com.stitch.gateway.security.model.Unsecured;
import com.stitch.gateway.security.service.AuthenticationService;
import com.stitch.user.model.dto.CustomerDto;
import com.stitch.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.stitch.gateway.util.Constants.BASE_URL;

@RestController
@RequestMapping(BASE_URL)
@CrossOrigin(origins = "http://localhost:4200/login")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @Unsecured
    @PostMapping("/customer-login")
    public LoginResponse customerLogin(@RequestBody @Valid LoginRequest loginRequest) {
        return authenticationService.authenticate(loginRequest);
    }

    @Unsecured
    @PostMapping("/request-token")
    public Token requestToken(@RequestParam("refreshToken") String refreshToken) {
        return authenticationService.refreshAccessToken(refreshToken);
    }

    @PostMapping("/create-pin")
    public Response createPin(@RequestParam("pin") String pin) {
        CustomerDto user = authenticationService.getAuthenticatedUser();
        return userService.createPin(user.getUserId(), pin.trim());
    }


    @PostMapping("/reset-pin-initiate-email")
    public Response resetPinInitiateEmail(@RequestParam("phoneNumber") String phoneNumber) {
        CustomerDto user = authenticationService.getAuthenticatedUser();
        return userService.resetPinInitiateEmail(user.getUserId(), phoneNumber.trim());
    }

    @PostMapping("/verify-reset-pin-code")
    public Response verifyResetPinCode(@RequestParam("code") String code) {
        CustomerDto user = authenticationService.getAuthenticatedUser();
        return userService.verifyResetPinCode(user.getUserId(), code.trim());
    }

    @PostMapping( "/reset-pin")
    public Response resetPin(@RequestParam("pin") String pin) {
        CustomerDto user = authenticationService.getAuthenticatedUser();
        return userService.resetPin(user.getUserId(), pin.trim());
    }
}
