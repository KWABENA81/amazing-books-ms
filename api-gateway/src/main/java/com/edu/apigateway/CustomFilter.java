package com.edu.apigateway;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class CustomFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain filterChain) {
        log.info("Initial Pre Global Filter...");
        return filterChain.filter(exchange);
//                .then(Mono.fromRunnable(() -> {
//                    log.info("...Last Post Global Filter...");
//                }));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
