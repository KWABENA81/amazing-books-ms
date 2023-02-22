package com.edu.bookmicroservice.service;


import com.edu.bookmicroservice.common.Issuer;
import com.edu.bookmicroservice.entity.Book;
import com.edu.bookmicroservice.repo.BookRepository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Optional;


@Service
public class BookService implements IBookService {

    private final Logger logger = LogManager.getLogger(BookService.class);
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
        logger.info(" BookService:  Book with id {} Deleted", id);
        bookRepository.deleteById(id);
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public Book addBook(Book book) {
        return findByIsbn(book.getIsbn()).map(bk -> {
            Integer copies = bk.getTotalCopies();
            bk.setTotalCopies(copies + book.getTotalCopies());
            return bookRepository.save(bk);
        }).orElseGet(() -> bookRepository.save(book));
    }

    public Book updateBook(Book book) {
        return findById(book.getId()).map(bk -> {
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
        /* To delete a book, ensure no copy of the book has been issued */
        try {
            if (!existIssuances(book)/*Delete(issuances.isEmpty()*/) {
                bookRepository.delete(book);
                logger.info("Issuer {} delete successful", book);
            } else throw new Exception("Invalid Delete Operation");
        } catch (Exception ex) {
            logger.error("Error delete, Issuer {} exception {}", book, ex.getMessage());
            return false;
        }
        return true;
    }


    @HystrixCommand(fallbackMethod = "hasIssuedBooksFallback",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000"),
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThrehold", value = "10")
            })
    private boolean hasIssuedBooks(String isbn) {
        String objUrl = issuerResourceIsbnUrl + isbn;
        ResponseEntity<Issuer> responseEntity = restTemplate.exchange(objUrl, HttpMethod.GET,
                new HttpEntity<Issuer>(createIssuerResourceHeaders()),
                Issuer.class);
        return (responseEntity != null);
    }

    private boolean existIssuances(Book book) {
        return true;
    }
    public boolean hasIssuedBooksFallback(String isbn) {
        IssuanceResponse issuanceResponse = new IssuanceResponse("none",
                new Issuer(999, "isbn", "none", 0),
                "Service Unavailable - This is a FallBack Response");
        return ResponseEntity.ok().body(issuanceResponse);
    }
    private HttpHeaders createIssuerResourceHeaders() {
        return new HttpHeaders() {
            {
                String auth = "User" + ":" + "password";
                byte[] encodedAuth = Base64.encodeBase64(
                        auth.getBytes(Charset.forName("US-ASCII")));

                String authHeader = "Basic " + new String(encodedAuth);
                set("Authorization", authHeader);
            }
        };
    }
}


