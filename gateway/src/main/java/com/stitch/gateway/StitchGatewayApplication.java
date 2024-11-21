package com.stitch.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;


@SpringBootApplication
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "com.stitch")
@EntityScan(basePackages = "com.stitch")
//@EnableScheduling
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.stitch", repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
public class StitchGatewayApplication {
	public static void main(String[] args) {

		DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
		LocalDateTime dateTime = LocalDateTime.parse("2024-07-16T08:03:50.533723", formatter);

		System.out.println(dateTime);

		SpringApplication.run(StitchGatewayApplication.class, args);
	}
}


