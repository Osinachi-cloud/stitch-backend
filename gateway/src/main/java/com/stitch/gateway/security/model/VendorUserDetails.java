package com.stitch.gateway.security.model;

import com.stitch.user.model.dto.CustomerDto;
import com.stitch.user.model.dto.VendorDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


public class VendorUserDetails implements UserDetails {

    private VendorDto user;
    private Collection<? extends GrantedAuthority> authorities;


    public VendorUserDetails(VendorDto user, List<GrantedAuthority> grantedAuthorities) {

        this.user = user;
        this.authorities = grantedAuthorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmailAddress();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.isAccountLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }


    public VendorDto getUser() {
        return user;
    }

}
