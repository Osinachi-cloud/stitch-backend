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

//@Slf4j
//@Service("customerUserDetailsService")
//public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {
//
//
//
//    @Autowired
//    private CustomerService customerService;
//
//    @Override
//    public UserDetails loadUserByUsername(String emailAddress) throws UsernameNotFoundException {
//
//        log.debug("Loading customer by email address [{}]", emailAddress);
//        if (StringUtils.isAnyBlank(emailAddress)) {
//            throw new UsernameNotFoundException("Email address is required");
//        }
//
//        CustomerDto user;
//        try {
//            user = customerService.getCustomerByEmail(emailAddress);
//
//        } catch (UserException e) {
//            log.error(e.getMessage(), e);
//            throw new UsernameNotFoundException(String.format("Customer not found for email address=%s", emailAddress));
//        }
//
//        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
//                .commaSeparatedStringToAuthorityList("CUSTOMER");
//        return new CustomUserDetails(user,
//                grantedAuthorities);
//    }
//
//
//}


//@Slf4j
//@Service("customUserDetailsService")
//public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {
//
//    @Autowired
//    private CustomerRepository customerRepository;
//
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<Customer> customer = customerRepository.findByEmailAddress(username);
//        log.info(" email val {} :", customer);
//
//        if(!customer.isPresent()){
//            throw new UsernameNotFoundException(String.format("customer not found for email address=%s", username));
//        }
////        log.info(" granted authorities {} :", customer.get().getAuthorities());
//
//        log.info("customer : {}", customer.get().getPassword());
//
//        CustomerDto customerDto = new CustomerDto(customer.get());
//        customerDto.setPassword(customer.get().getPassword());
//
//        PermissionDto permission = new PermissionDto();
//        permission.setName(customer.get().getRole().getName());
//        permission.setDescription(customer.get().getRole().getDescription());
//
//
//        RoleDto roleDto = new RoleDto(customer.get().getRole());
//        List<PermissionDto> permissionDtos = new ArrayList<>();
//        permissionDtos.add(permission);
//        roleDto.setPermissionsDto(permissionDtos);
//        customerDto.setRole(roleDto);
//
//
//
//
//        log.info("customerDto detail : {}", customerDto);
//
//        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
//
////        if(customerDto.getRole().getPermissionsDto() != null){
////            for(PermissionDto permissionDto: customerDto.getRole().getPermissionsDto()){
////                grantedAuthorities.add(new SimpleGrantedAuthority(permissionDto.getName()));
////            }
////        }
//
//        if(customerDto.getRole() != null){
//            System.out.println( "customerDto.getRole() =================== :" + customerDto.getRole());
////            for(RoleDto roleDto1: customerDto.getRole()){
////                grantedAuthorities.add(new SimpleGrantedAuthority(customerDto.getRole().getName().toLowerCase()));
////            grantedAuthorities.add(new SimpleGrantedAuthority(customerDto.getRole().getName()));
//            grantedAuthorities.add(new SimpleGrantedAuthority("VENDOR"));
//
////            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" +"customer"));
//
////            }
//        }
//
//        log.info("grantedAuthorities : {}", grantedAuthorities);
//
//
//
//        return new CustomUserDetails(customerDto, grantedAuthorities);
//
//    }
//
//
//}








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
