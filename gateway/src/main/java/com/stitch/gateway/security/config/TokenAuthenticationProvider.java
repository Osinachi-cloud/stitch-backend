package com.stitch.gateway.security.config;

import com.stitch.gateway.security.util.TokenUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class TokenAuthenticationProvider {

    @Qualifier("customUserDetailsService")
    private final CustomUserDetailsServiceImpl customUserDetailsService;


    private final TokenUtils tokenUtils;

    public TokenAuthenticationProvider(@Qualifier("customUserDetailsService") CustomUserDetailsServiceImpl customUserDetailsService, TokenUtils tokenUtils) {
        this.customUserDetailsService = customUserDetailsService;
        this.tokenUtils = tokenUtils;
    }

    public Authentication authenticate(String token){
        System.out.println("entered authenticate method");
        Claims claims = tokenUtils.validateAccessToken(token);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername((String) claims.get("email"));

        System.out.println("+++++++++++++++++++++++++++");
        System.out.println(userDetails.getUsername() + userDetails.getAuthorities());
        System.out.println("+++++++++++++++++++++++++++");

        if (!userDetails.isEnabled()){
            throw new DisabledException("User is disabled");
        }

        if (!userDetails.isAccountNonLocked()){
            throw new LockedException("User account is locked");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

//    public Authentication authenticate(String token) {
//        System.out.println("Entered authenticate method");
//
//        Claims claims = tokenUtils.validateAccessToken(token);
//        String email = (String) claims.get("email");
//        UserDetails userDetails = null;
//
//        try {
//            userDetails = customUserDetailsService.loadUserByUsername(email);
//        } catch (UsernameNotFoundException e) {
//            System.out.println("User not found in customUserDetailsService, trying vendorUserDetailsService");
//            try {
//                userDetails = vendorUserDetailsService.loadUserByUsername(email);
//            } catch (Exception vendorException) {
//                System.out.println("User not found in vendorUserDetailsService either");
//                throw new UsernameNotFoundException("User not found in any user details service");
//            }
//        } catch (Exception otherException) {
//            System.out.println("An unexpected error occurred while loading user details");
//            throw otherException; // Re-throw other exceptions
//        }
//
//        System.out.println("+++++++++++++++++++++++++++");
//        System.out.println(userDetails.getUsername());
//        System.out.println("+++++++++++++++++++++++++++");
//
//        if (!userDetails.isEnabled()) {
//            throw new DisabledException("User is disabled");
//        }
//
//        if (!userDetails.isAccountNonLocked()) {
//            throw new LockedException("User account is locked");
//        }
//
//        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//    }
//
//    public Authentication vendorAuthenticate(String token){
//        System.out.println("entered authenticate method");
//        Claims claims = tokenUtils.validateAccessToken(token);
//        UserDetails userDetails = vendorUserDetailsService.loadUserByUsername((String) claims.get("email"));
//
//        if (!userDetails.isEnabled()){
//            throw new DisabledException("User is disabled");
//        }
//
//        if (!userDetails.isAccountNonLocked()){
//            throw new LockedException("User account is locked");
//        }
//
//        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//    }
}



//@Component
//@Slf4j
//public class TokenAuthenticationProvider implements AuthenticationProvider {
//
////    private final CustomerService customerService;
////    private final VendorService vendorService;
//
//    @Qualifier("customerUserDetailsService")
//    private final CustomUserDetailsService customerService;
//
//    @Qualifier("vendorUserDetailsService")
//    private final CustomUserDetailsService vendorService;
//
//    public TokenAuthenticationProvider(@Qualifier("customerUserDetailsService") CustomUserDetailsService customerService, @Qualifier("vendorUserDetailsService") CustomUserDetailsService vendorService) {
//        this.customerService = customerService;
//        this.vendorService = vendorService;
//    }
//
//
////    @Autowired
////    public TokenAuthenticationProvider(CustomerService customerService, VendorService vendorService) {
////        this.customerService = customerService;
////        this.vendorService = vendorService;
////    }
//
//    @Override
//    public Authentication authenticate(Authentication authentication)  {
//        String token = (String) authentication.getCredentials();
//        Class<? extends UserDetails> userDetailsClass;
//
//        log.info("authentication 3  : {}",  authentication.getPrincipal());
//        log.info("authentication 4  : {}",  authentication.isAuthenticated());
//        log.info("authentication 5  : {}",  authentication.getAuthorities());
//
//        if (authentication.getPrincipal() instanceof Customer){
//            System.out.println("it is a customer");
//            userDetailsClass = CustomUserDetails.class;
//        } else if (authentication.getPrincipal() instanceof Vendor) {
//            System.out.println("it is a vendor");
//            userDetailsClass = VendorUserDetails.class;
//        } else {
//            System.out.println("it is a  not");
//            throw new AuthenticationServiceException("Invalid authentication request");
//        }
//
//        UserDetails userDetails;
//
//        try {
//            if (userDetailsClass == CustomUserDetails.class) {
//                userDetails = customerService.loadUserByUsername(token);
//            } else {
//                userDetails = vendorService.loadUserByUsername(token);
//            }
//        } catch (UsernameNotFoundException e) {
//            throw new AuthenticationServiceException("Invalid authentication request", e);
//        }
//
//        if (userDetails == null) {
//            throw new AuthenticationServiceException("Invalid authentication request");
//        }
//
//        return new UsernamePasswordAuthenticationToken(
//                userDetails,
//                userDetails.getPassword(),
//                userDetails.getAuthorities());
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return authentication.equals(UsernamePasswordAuthenticationToken.class);
//    }
//}
