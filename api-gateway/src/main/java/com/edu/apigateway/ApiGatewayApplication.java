package com.edu.apigateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Slf4j
@SpringBootApplication
@EnableEurekaClient
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        log.info("Creating new BCryptPasswordEncoder");
//        return new BCryptPasswordEncoder();
//    }

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/get")
                        .filters(f -> f.addRequestHeader("Hello", "World"))
                        .uri("https://httpbin.org:80"))
                .route(p -> p
                        .host("*.circuitbreaker.com")
                        .filters(f -> f.circuitBreaker(config -> config.setName("mycmd")))
                        .uri("https://httpbin.org:80"))
                .build();
    }

    @RequestMapping("/fallback")
    public Mono<String> fallback() {
        return Mono.just("fallback");
    }
}
