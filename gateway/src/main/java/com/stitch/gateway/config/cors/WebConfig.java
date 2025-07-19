package com.stitch.gateway.config.cors;


import com.stitch.gateway.util.EnvProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final EnvProperties properties;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(properties.getAllowedOrigins().toArray(new String[0]))
                .allowedMethods(properties.getAllowedMethods().toArray(new String[0]))
                .allowedHeaders(properties.getAllowedHeaders().toArray(new String[0]))
                .allowCredentials(properties.isAllowCORS());
    }
}
