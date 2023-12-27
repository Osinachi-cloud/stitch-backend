package com.stitch.gateway.security.config;

import com.stitch.user.exception.UserException;
import com.stitch.user.model.dto.CustomerDto;
import com.stitch.user.service.CustomerService;
import com.stitch.gateway.security.model.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service("userDetailsService")
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    @Autowired
    private CustomerService customerService;

    @Override
    public UserDetails loadUserByUsername(String emailAddress) throws UsernameNotFoundException {

        log.debug("Loading customer by email address [{}]", emailAddress);
        if (StringUtils.isAnyBlank(emailAddress)) {
            throw new UsernameNotFoundException("Email address is required");
        }

        CustomerDto user;
        try {
            user = customerService.getCustomerByEmail(emailAddress);

        } catch (UserException e) {
            log.error(e.getMessage(), e);
            throw new UsernameNotFoundException(String.format("Customer not found for email address=%s", emailAddress));
        }

        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("CUSTOMER");
        return new CustomUserDetails(user,
                grantedAuthorities);
    }


}
