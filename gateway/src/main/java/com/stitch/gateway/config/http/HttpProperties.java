package com.stitch.gateway.config.http;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Configuration
@ConfigurationProperties(prefix = "http")
//@RefreshScope
public class HttpProperties {

    private Integer maxTotal;
    private Integer maxPerRoute;
    private Integer connectionTimeout;
    private Integer socketTimeout;
    private Integer connectionRequestTimeout;
}
