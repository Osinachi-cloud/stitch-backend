package com.stitch.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "com.stitch")
@EnableJpaRepositories(basePackages = "com.stitch")
@EntityScan(basePackages = "com.stitch")
@EnableScheduling
public class StitchGatewayApplication {
	public static void main(String[] args) {

		SpringApplication.run(StitchGatewayApplication.class, args);
	}
}
