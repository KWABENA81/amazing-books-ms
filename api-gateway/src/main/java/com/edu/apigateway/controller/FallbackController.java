package com.edu.apigateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

    @RequestMapping("/bookFallBack")
    public Mono<String> bookMicroserviceFallBack() {
        return Mono.just("Book Service is taking too long to respond.  Please try again later.");
    }

    @RequestMapping("/issuerFallBack")
    public Mono<String> issuerMicroserviceFallBack() {
        return Mono.just("Issuer Service is taking too long to respond.  Please try again later.");
    }

}
