package com.stitch.gateway.security.config;




import org.springframework.security.core.userdetails.UserDetails;
        import org.springframework.security.core.userdetails.UserDetailsService;
        import org.springframework.security.core.userdetails.UsernameNotFoundException;
        import org.springframework.stereotype.Service;
        import java.util.ArrayList;
        import java.util.List;

@Service("compositeUserDetailsServices")
public class CompositeUserDetailsService implements UserDetailsService {

    private final List<UserDetailsService> userDetailsServiceList;

    public CompositeUserDetailsService(List<UserDetailsService> userDetailsServiceList) {
        this.userDetailsServiceList = userDetailsServiceList;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        for (UserDetailsService service : userDetailsServiceList) {
            try {
                System.out.println("great one");
                return service.loadUserByUsername(username);
            } catch (UsernameNotFoundException ignored) {
                // User not found in this service, continue to the next one
            }
        }
        throw new UsernameNotFoundException("User not found");
    }
}