package com.edu.bookmicroservice.controller;


import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.*;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@EnableSwagger2
@EnableWebMvc
@Component
public class SwaggerConfig {

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("book-controller")
                .apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.edu.bookmicroservice.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Book API").description("Book API")
                .contact(new Contact("Solomon S", "https://github.com/KWABENA81", "kwabena81@yahoo.com"))
                .version("1.0").build();
    }

    private boolean shouldRegisterLinksMapping(WebEndpointProperties webEndpointProperties,
                                               Environment environment, String basePath) {
        return webEndpointProperties.getDiscovery().isEnabled()
                && (StringUtils.hasText(basePath) ||
                ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));
    }

    @Bean
    public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(
            WebEndpointsSupplier weps, ServletEndpointsSupplier seps,
            ControllerEndpointsSupplier ceps, EndpointMediaTypes epmt,
            CorsEndpointProperties corsProps, WebEndpointProperties wepp, Environment env) {
        List<ExposableEndpoint<?>> exposableEndPoints = new ArrayList();
        Collection<ExposableWebEndpoint> exposableWebEndpoints = weps.getEndpoints();
        exposableEndPoints.addAll(exposableWebEndpoints);
        exposableEndPoints.addAll(seps.getEndpoints());
        exposableEndPoints.addAll(ceps.getEndpoints());
        String basePath = wepp.getBasePath();
        EndpointMapping endpointMapping = new EndpointMapping(basePath);
        boolean shouldRegisterLinksMapping = this.shouldRegisterLinksMapping(wepp, env, basePath);
        return new WebMvcEndpointHandlerMapping(endpointMapping, exposableWebEndpoints, epmt,
                corsProps.toCorsConfiguration(), new EndpointLinksResolver(exposableEndPoints, basePath),
                shouldRegisterLinksMapping/*, null*/);
    }
}
