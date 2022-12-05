package com.dinamatica.pabs.hackertest.spring.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@ComponentScan(basePackages = "com.hotelbeds.supplierintegrations.hackertest")
@PropertySource("classpath:application.properties")
public class AppConfig {

}
