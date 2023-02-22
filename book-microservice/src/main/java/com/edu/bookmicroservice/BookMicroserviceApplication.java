package com.edu.bookmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
//@EnableWebMvc
@EnableEurekaClient
@EnableWebSecurity
public class BookMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookMicroserviceApplication.class, args);
    }

    @Bean //    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}
