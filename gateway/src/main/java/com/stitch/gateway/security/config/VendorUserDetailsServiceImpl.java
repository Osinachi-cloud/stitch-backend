package com.stitch.gateway.security.config;

import com.stitch.gateway.security.model.CustomUserDetails;
import com.stitch.gateway.security.model.VendorUserDetails;
import com.stitch.user.exception.UserException;
import com.stitch.user.model.dto.CustomerDto;
import com.stitch.user.model.dto.VendorDto;
import com.stitch.user.service.CustomerService;
import com.stitch.user.service.VendorService;
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
@Service("vendorUserDetailsService")
public class VendorUserDetailsServiceImpl implements CustomUserDetailsService {

    @Autowired
    private VendorService vendorService;

    @Override
    public UserDetails loadUserByUsername(String emailAddress) throws UsernameNotFoundException {

        log.debug("Loading vendor by email address [{}]", emailAddress);
        if (StringUtils.isAnyBlank(emailAddress)) {
            throw new UsernameNotFoundException("Email address is required");
        }

        VendorDto user;
        try {
            user = vendorService.getVendorByEmail(emailAddress);

        } catch (UserException e) {
            log.error(e.getMessage(), e);
            throw new UsernameNotFoundException(String.format("Vendor not found for email address=%s", emailAddress));
        }

        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("VENDOR");
        return new VendorUserDetails(user,
                grantedAuthorities);
    }


}
