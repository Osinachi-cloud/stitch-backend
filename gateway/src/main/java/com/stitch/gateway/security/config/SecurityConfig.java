package com.stitch.gateway.security.config;

import com.stitch.gateway.exception.CustomAccessDeniedHandler;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    private TokenAuthenticationProvider tokenAuthenticationProvider;

    @Autowired
    private  TokenAuthenticationFilter tokenAuthenticationFilter;

//    @Autowired
//    private CustomAccessDeniedHandler customAccessDeniedHandler;

//    @Autowired
//    private PasswordEncoder passwordEncoder;


//    @Override
//    @Bean
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println(" entered security config");
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth->{
                    auth
                            .requestMatchers(
//                                    "/h2-console/**",
//                                    "/api/v1/auth/**",
//                                    "/api/v1/merchant/**",
//                                    "/v2/api-docs",
//                                    "/v3/api-docs",
//                                    "/v3/api-docs/**",
//                                    "/swagger-resources",
//                                    "/swagger-resources/**",
//                                    "/configuration/ui",
//                                    "/configuration/security",
//                                    "/swagger-ui/**",
//                                    "/webjars/**",
//                                    "/configuration/**",
//                                    "/swagger-ui.html",
                                    "/altair",
                                    "/actuator/health",
                                    "/graphql",
                                    "/vendor/**"
                            )
                            .permitAll()
//                            .requestMatchers("/graphql").permitAll()
//                            .requestMatchers("/altair").permitAll()
//                            .requestMatchers("/vendor/**").permitAll()
//                            .requestMatchers("/actuator/health").permitAll()
//                            .requestMatchers("/admin/**").hasRole("ADMIN")  // Requires ADMIN role
                            .anyRequest().authenticated();
                })
                .exceptionHandling()
//                .accessDeniedHandler(new CustomAccessDeniedHandler())
                .accessDeniedHandler(accessDeniedHandler())
                .and()
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authenticationProvider(authenticationProvider())
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

//        private AccessDeniedHandler accessDeniedHandler() {
//        return (httpServletRequest, httpServletResponse, e) -> {
//            log.warn("Access denied: {}", e.getMessage());
//        };
//    }

    private AccessDeniedHandler accessDeniedHandler() {
        return (httpServletRequest, httpServletResponse, e) -> {
            log.warn("Access denied: {}", e.getMessage());
            httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
        };
    }



//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsServiceImpl());
//        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }


    @Bean
    public UserDetailsService userDetailsServiceImpl() {
        return new CustomUserDetailsServiceImpl();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}