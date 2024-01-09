package com.stitch.gateway.config.cors;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Value("${cors.allowed-credential}")
    private boolean allowedCredential;

    @Value("${cors.allowed-origin}")
    private String allowedOrigin;

    @Value("${cors.allowed-header}")
    private String allowedHeader;

    @Value("${cors.allowed-method}")
    private String allowedMethod;

//    @Bean
//    public CorsFilter corsFilter() {
//        System.out.println("=====>>>>" +allowedOrigin);
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(allowedCredential);
//        config.addAllowedOrigin(allowedOrigin);
//        config.addAllowedHeader(allowedHeader);
////        config.addAllowedMethod(allowedMethod);
//        config.addAllowedMethod("OPTIONS");
//        source.registerCorsConfiguration("/**", config);
//        return new CorsFilter(source);
//    }


    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:4200");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/graphql/**", config);
        return new CorsFilter(source);
    }

}