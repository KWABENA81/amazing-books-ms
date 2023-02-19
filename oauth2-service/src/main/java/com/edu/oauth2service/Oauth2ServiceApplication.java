package com.edu.oauth2service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableEurekaClient
@EnableWebSecurity
public class Oauth2ServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(Oauth2ServiceApplication.class, args);
    }

}
