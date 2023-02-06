package com.edu.apigateway.api;

import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

public class FallbackController {

    @RequestMapping("/bookFallBack")
    public Mono<String> bookMicroServiceFallBack() {
        return Mono.just("Book Service is taking too long to respond.  Please try again later.");
    }

    @RequestMapping("/issuerFallBack")
    public Mono<String> issuerMicroServiceFallBack() {
        return Mono.just("Issuer Service is taking too long to respond.  Please try again later.");
    }

    @RequestMapping("/oauthFallBack")
    public Mono<String> oauthMicroServiceFallBack() {
        return Mono.just("Oauth Service is taking too long to respond.  Please try again later.");
    }
}
