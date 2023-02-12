package com.edu.bookmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
//@EnableWebMvc
@EnableEurekaClient
//@EnableWebSecurity
public class BookMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookMicroserviceApplication.class, args);

        //	Initial data load test
		/*ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(BookMicroserviceApplication.class, args);
        BookRepository bookRepository = configurableApplicationContext
                .getBean(BookRepository.class);
        Book book = new Book(111, "7y0opds5ts9",
                "Animal Kingdom", LocalDate.now(),
                12, 1, "John Smith");
        bookRepository.save(book);*/
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}
