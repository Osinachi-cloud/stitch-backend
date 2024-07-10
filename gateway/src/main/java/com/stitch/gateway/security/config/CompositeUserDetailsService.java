package com.stitch.gateway.security.config;


//@Service("compositeUserDetailsServices")
//public class CompositeUserDetailsService implements UserDetailsService {
//
//    private final List<UserDetailsService> userDetailsServiceList;
//
//    public CompositeUserDetailsService(List<UserDetailsService> userDetailsServiceList) {
//        this.userDetailsServiceList = userDetailsServiceList;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        for (UserDetailsService service : userDetailsServiceList) {
//            try {
//                System.out.println("great one");
//                return service.loadUserByUsername(username);
//            } catch (UsernameNotFoundException ignored) {
//                // User not found in this service, continue to the next one
//            }
//        }
//        throw new UsernameNotFoundException("User not found");
//    }
//}


//@Slf4j
//@Service("userDetailsService")
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
//        if(!customer.isPresent()){
//            throw new UsernameNotFoundException(String.format("customer not found for email address=%s", username));
//        }
//        log.info("customer : {}", customer.get().getPassword());
//
//        CustomerDto customerDto = new CustomerDto(customer.get());
//        RoleDto roleDto = new RoleDto(customer.get().getRole());
//        customerDto.setRole(roleDto);
//
//        log.info("customerDto detail : {}", customerDto);
//
//        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
//
//        if(customerDto.getRole().getPermissionsDto() != null){
//            for(PermissionDto permissionDto: customerDto.getRole().getPermissionsDto()){
//                grantedAuthorities.add(new SimpleGrantedAuthority(permissionDto.getName()));
//            }
//        }
//
//        log.info("grantedAuthorities : {}", grantedAuthorities);
//
//
//        return new CustomUserDetails(customerDto, grantedAuthorities);
//
//    }
//
//
//}
