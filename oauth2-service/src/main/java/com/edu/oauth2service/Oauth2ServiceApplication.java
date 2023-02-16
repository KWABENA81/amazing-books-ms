package com.edu.oauth2service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class Oauth2ServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(Oauth2ServiceApplication.class, args);
    }

}
