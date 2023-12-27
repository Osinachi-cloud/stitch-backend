//package com.stitch.gateway.config.redis;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//
//@EnableCaching
//@Configuration
//public class ConnectionConfig {
//
//    @Autowired
//    private RedisProperties redisProperties;
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
//        configuration.setHostName(redisProperties.getHost());
//        configuration.setPort(redisProperties.getPort());
//        configuration.setDatabase(redisProperties.getDatabase());
//        configuration.setUsername(redisProperties.getUsername());
//        configuration.setPassword(redisProperties.getPassword());
//        return new LettuceConnectionFactory(configuration);
//    }
//}
