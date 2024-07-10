package com.stitch.gateway.security.model;

import com.stitch.user.model.dto.CustomerDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class CustomUserDetails implements UserDetails {

    private CustomerDto user;

    private Collection<? extends GrantedAuthority> authorities;


    public CustomUserDetails(CustomerDto user, Set<GrantedAuthority> grantedAuthorities) {
        System.out.println("entered CustomUserDetails constructor");
        this.user = user;
//        System.out.println(user?.getProfileImage().length());
        this.authorities = grantedAuthorities;
        System.out.println(grantedAuthorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        System.out.println("autho================");
        System.out.println(authorities);
        System.out.println("autho================");
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

//        return !user.isAccountLocked();
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {

//        return user.isEnabled();
        return true;
    }


    public CustomerDto getUser() {
        return user;
    }

}
