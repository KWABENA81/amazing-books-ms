package com.edu.issuermicroservice.service;


import com.edu.issuermicroservice.common.Book;
import com.edu.issuermicroservice.common.IssuanceRequest;
import com.edu.issuermicroservice.common.IssuanceResponse;
import com.edu.issuermicroservice.entity.Issuer;
import com.edu.issuermicroservice.repo.IssuerRepository;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.*;

@Service
public class IssuerService implements IIssuerService {
    private final Logger logger = LogManager.getLogger(IssuerService.class);
    public static final String FS = "-";
    public final String bookResourceUpdateUrl = "http://localhost:8097/book/edit/";
    public final String bookResourceIsbnUrl = "http://localhost:8097/book/isbn/";
    public final String bookResourceIsbnLBUrl = "lb://BOOK-MICROSERVICE/isbn/";

    @Autowired
    private IssuerRepository issuerRepository;

    @Autowired
    @Lazy
    private RestTemplate restTemplate;


    public Collection<Issuer> findAll() {
        logger.info("IssuerService : started to collect all issuers");
        return issuerRepository.findAll();
    }

    @Override
    public Optional<Issuer> findById(Integer id) {
        logger.info("IssuerService : started to collect issuance with id, {}", id);
        return issuerRepository.findById(id);
    }

    @Override
    public boolean delete(Integer id) {
        try {
            issuerRepository.deleteById(id);
            logger.info("Issuer {} delete successful", id);
        } catch (Exception ex) {
            logger.error("Error delete, Issuer {} exception {}", id, ex.getMessage());
            return false;
        }
        return true;
    }

    private String issuanceProcessing() {
        return new Random().nextBoolean() ? "success" : "failure";
    }

    public IssuanceResponse doIssuance(IssuanceRequest request) {
        Book book = fetchBookByIsbn(request.getIssuer().getIsbn()).getBody();
        IssuanceResponse issuanceResponse = null;
        if (book != null && canIssueBook(book, request)) {
            Issuer issuer = new Issuer();
            String custId = customerInfo(request);
            issuer.setCustId(custId.toUpperCase());
            issuer.setNoOfCopies(request.getIssuer().getNoOfCopies());
            //  save issuer
            issuerRepository.save(issuer);
            //  update book record
            Integer oldQty = book.getIssuedCopies();
            book.setIssuedCopies(oldQty + request.getIssuer().getNoOfCopies());
            /* ResponseEntity<*/
            IssuanceResponse/*>*/ issuanceResponseRE = updateBook(book).getBody();

            logger.info("ResponseEntity<IssuanceResponse> of book Update {}", issuanceResponseRE);
            issuanceResponse = new IssuanceResponse();
            issuanceResponse.setIssuer(issuer);

            issuanceResponse.setCustomerInfo(custId);
            issuanceResponse.setIssuanceStatus("SUCCESS");
        }
        return issuanceResponse;
    }

    private String customerInfo(IssuanceRequest request) {
        StringBuilder sbuilder = new StringBuilder();

        sbuilder.append(request.getIssuer().getCustId()).append(FS)
                .append(UUID.randomUUID()).append(FS)
                .append(issuanceProcessing());
        return sbuilder.toString().toUpperCase();
    }

    private boolean canIssueBook(Book book, IssuanceRequest request) {
        return book != null && request != null
                && ((book.getTotalCopies() - book.getIssuedCopies()) > request.getIssuer().getNoOfCopies());
    }


    public List<Issuer> findIssuancesByIsbn(String isbn) {
        return issuerRepository.findIssuanceByBookIsbn(isbn);
    }


    @HystrixCommand(fallbackMethod = "fetchBookFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "11100"),
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "500"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "100")
    })
    public ResponseEntity<Book> fetchBookByIsbn(@PathVariable(value = "isbn") String isbn) {
        logger.info("http://localhost:8097/book/isbn/" + isbn);
        String url = bookResourceIsbnUrl/*bookResourceIsbnLBUrl*/ + isbn;
        ResponseEntity<Book> responseEntity
                = restTemplate.exchange(url, HttpMethod.GET,
                new HttpEntity<Book>(createBookResourceHeaders("User", "password")),
                Book.class);
        return responseEntity;
    }

    public ResponseEntity<Book> fetchBookFallback(@PathVariable(value = "isbn") String isbn) {
        Book book = new Book(999, isbn, "THIS_AUTHOR",
                "Service Unavailable - This is a FallBack Response",
                LocalDate.now(), 1000, 0);
        return ResponseEntity.ok().body(book);
    }


    @HystrixCommand(fallbackMethod = "updateBookFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000"),
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "500"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "100")
    })
    private ResponseEntity<IssuanceResponse> updateBook(Book book) {
        String bookResourceUpdateUrl = this.bookResourceUpdateUrl + book.getId();
        ResponseEntity<IssuanceResponse> responseEntity
                = restTemplate.exchange(bookResourceUpdateUrl, HttpMethod.PUT,
                new HttpEntity<IssuanceResponse>(createBookResourceHeaders("User", "password")),
                IssuanceResponse.class);
        return responseEntity;
    }

    public ResponseEntity<IssuanceResponse> updateBookFallback(Book book) {
        IssuanceResponse issuanceResponse = new IssuanceResponse("none",
                new Issuer(999, "isbn", "none", 0),
                "Service Unavailable - This is a FallBack Response");
        return ResponseEntity.ok().body(issuanceResponse);
    }

    public boolean updateIssuance(Issuer issuer) {
        boolean hasUpdatedBook = false;
        //  obtain list of the issued books
        List<Issuer> issuances = findIssuancesByIsbn(issuer.getIsbn());

        //  get issuer with isbn & containing customer Id
        Optional<Issuer> issuanceOptional = issuances.stream()
                .filter(storedIssuance -> storedIssuance.getCustId()
                        .contains(issuer.getCustId())).findFirst();

        if (issuanceOptional.isPresent()) {
            Issuer issuance = issuanceOptional.get();
            try {
                int adjQty = issuer.getNoOfCopies() - issuance.getNoOfCopies();
                if (adjQty == 0)
                    return hasUpdatedBook;

                Book book = fetchBookByIsbn(issuance.getIsbn()).getBody();
                int issuedCopies = book.getIssuedCopies();

                book.setIssuedCopies(issuedCopies + adjQty);
                IssuanceResponse bookResponse = updateBook(book).getBody();
                logger.info("Response of Book Update; {}", bookResponse);

                issuance.setNoOfCopies(issuer.getNoOfCopies());
                Issuer issuerUpdate = issuerRepository.save(issuance);
                logger.info("Response of Book Update; {}", issuerUpdate);
                hasUpdatedBook = true;
            } catch (Exception ex) {
                return hasUpdatedBook;
            }
        }
        return hasUpdatedBook;
    }


    private HttpHeaders createBookResourceHeaders(final String uname, final String pword) {
        return new HttpHeaders() {
            {
                String auth = uname + ":" + pword;
                byte[] encodedAuth = Base64.encodeBase64(
                        auth.getBytes(Charset.forName("US-ASCII")));

                String authHeader = "Basic " + new String(encodedAuth);
                set("Authorization", authHeader);
            }
        };
    }
}

