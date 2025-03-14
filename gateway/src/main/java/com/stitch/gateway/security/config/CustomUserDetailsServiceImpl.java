package com.stitch.gateway.security.config;

import com.stitch.user.model.dto.CustomerDto;
import com.stitch.user.model.dto.PermissionDto;
import com.stitch.user.model.entity.UserEntity;
import com.stitch.user.repository.UserRepository;
import com.stitch.gateway.security.model.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;



@Slf4j
@Service("customUserDetailsService")
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    @Autowired
    private UserRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> customer = customerRepository.findByEmailAddress(username);
        log.info(" email val {} :", customer);

        if (!customer.isPresent()) {
            throw new UsernameNotFoundException(String.format("customer not found for email address=%s", username));
        }

        CustomerDto customerDto = new CustomerDto(customer.get());
        customerDto.setPassword(customer.get().getPassword());

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        if(customerDto.getRole().getPermissionsDto() != null){
            for(PermissionDto permissionDto: customerDto.getRole().getPermissionsDto()){
                grantedAuthorities.add(new SimpleGrantedAuthority(permissionDto.getName()));
            }
        }

        log.info("grantedAuthorities : {}", grantedAuthorities);

        return new CustomUserDetails(customerDto, grantedAuthorities);
    }
}
