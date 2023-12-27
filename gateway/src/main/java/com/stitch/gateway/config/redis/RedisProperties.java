package com.stitch.gateway.config.redis;


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
@ConfigurationProperties(prefix = "redis")
//@RefreshScope
public class RedisProperties {

    private String host;
    private Integer port;
    private Integer database;
    private String username;
    private String password;
}
