package com.edu.bookmicroservice.service;


import com.edu.bookmicroservice.common.Issuer;
import com.edu.bookmicroservice.entity.Book;
import com.edu.bookmicroservice.repo.BookRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class BookService implements IBookService {
    private final String issuerResourceIsbnUrl = "http://localhost:8099/issuances/isbn/";
    private final String issuerResourceIsbnLBUrl = "http://ISSUER-MICROSERVICE/issuances/isbn/";
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    @Lazy
    private RestTemplate restTemplate;

    @Override
    public Collection<Book> findAll() {
        return bookRepository.findAll();
    }


    @Override
    public Optional<Book> findById(Integer id) {
        return bookRepository.findById(id);
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    @Override
    public void delete(Integer id) {
        log.info(" BookService:  Book with id {} Deleted", id);
        bookRepository.deleteById(id);
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public Book addBook(Book book) {
        return findByIsbn(book.getIsbn()).map(bk -> {
            //            if found
            Integer copies = bk.getTotalCopies();
            bk.setTotalCopies(copies + book.getTotalCopies());
            return bookRepository.save(bk);
        }).orElseGet(() -> {
            return bookRepository.save(book);
        });
    }

    public Book updateBook(Book book) {
        return findById(book.getId()).map(bk -> {
                    //            if found
                    bk.setTotalCopies(book.getTotalCopies());
                    bk.setIssuedCopies(book.getIssuedCopies());
                    return bookRepository.save(bk);
                })
                .orElseGet(() -> null);
    }


//    @HystrixCommand(fallbackMethod = "deleteBookFallback",
//            commandProperties = {
//                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000"),
//                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
//                    @HystrixProperty(name = "circuitBreaker.requestVolumeThrehold", value = "10")
//            })
    public boolean deleteBook(Book book) {
        //  to delete book, first check issuances
        try {
            List<Issuer> issuances = fetchIssuanceByIsbn(book.getIsbn());
            if (issuances.isEmpty()) {
                bookRepository.delete(book);
                log.info("Issuer {} delete successful", book);
            } else throw new Exception("Invalid Delete Operation");
        } catch (Exception ex) {
            log.error("Error delete, Issuer {} exception {}", book, ex.getMessage());
            return false;
        }
        return true;
    }


//    @HystrixCommand(fallbackMethod = "issuancesFallback",
//            commandProperties = {
//                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000"),
//                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
//                    @HystrixProperty(name = "circuitBreaker.requestVolumeThrehold", value = "10")
//            })
    private List<Issuer> fetchIssuanceByIsbn(String isbn) {
        String objUrl = issuerResourceIsbnUrl + isbn;
        Issuer[] issuances = null;
        try {
            ResponseEntity<Issuer[]> response = restTemplate.getForEntity(objUrl, Issuer[].class);
            issuances = response.getBody();

            log.info("Issuer fetchBookByIsbn : {}", new ObjectMapper().writeValueAsString(issuances));
            return Arrays.asList(issuances);
        } catch (JsonProcessingException jex) {
            log.error("Issuer fetchBookByIsbn failed: {} : {}", objUrl, jex.getMessage());
            return null;
        }


//        public Object deleteBookFallback () {
//            List<Issuer> issuerList = new ArrayList<>();
//            return issuerList;
//        }
    }

}
