package com.edu.issuermicroservice.service;


import com.edu.issuermicroservice.common.Book;
import com.edu.issuermicroservice.common.IssuanceRequest;
import com.edu.issuermicroservice.common.IssuanceResponse;
import com.edu.issuermicroservice.model.Issuer;
import com.edu.issuermicroservice.repo.IssuerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class IssuerService implements IIssuerService {

    private static final String FS = "#";
    private final String bookResourceUpdateUrl = "http://localhost:8097/books/edit/";
    private final String bookResourceIsbnUrl = "http://localhost:8097/books/isbn/";
    private final String bookResourceIsbnLBUrl = "http://BOOK-MICROSERVICE/books/isbn/";

    @Autowired
    private IssuerRepository issuerRepository;

    @Autowired
    @Lazy
    private RestTemplate restTemplate;


    public Collection<Issuer> findAll() {
        return issuerRepository.findAll();
    }

    @Override
    public Optional<Issuer> findById(Integer id) {
        return issuerRepository.findById(id);
    }

    @Override
    public boolean delete(Integer id) {
        try {
            issuerRepository.deleteById(id);
            log.info("Issuer {} delete successful", id);
        } catch (Exception ex) {
            log.error("Error delete, Issuer {} exception {}", id, ex.getMessage());
            return false;
        }
        return true;
    }

    private String issuanceProcessing() {
        return new Random().nextBoolean() ? "success" : "failure";
    }

    public IssuanceResponse doIssuance(IssuanceRequest request) {
        Book book = fetchBookByIsbn(request.getIssuer().getIsbn());
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
            ResponseEntity<IssuanceResponse> issuanceResponseRE = updateBook(book);

            log.info("ResponseEntity<IssuanceResponse> of book Update {}", issuanceResponseRE);
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

    public Book fetchBookByIsbn(String isbn) {
        String objUrl = bookResourceIsbnUrl + isbn;
        Book book = new Book();
        try {
            book = restTemplate.getForObject(objUrl, Book.class);
            log.info("Issuer fetchBookByIsbn : {}", new ObjectMapper().writeValueAsString(book));
        } catch (JsonProcessingException jex) {
            log.error("Issuer fetchBookByIsbn failed: {} : {}", objUrl, jex.getMessage());
        } finally {
            return book;
        }
    }

    private ResponseEntity<IssuanceResponse> updateBook(Book book) {
        String bookResourceUpdateUrl = this.bookResourceUpdateUrl + book.getId();
        ResponseEntity<IssuanceResponse> responseEntity
                = restTemplate.exchange(bookResourceUpdateUrl, HttpMethod.PUT,
                new HttpEntity<IssuanceResponse>(createHeaders("user", "Password")),
                IssuanceResponse.class);
        return responseEntity;
    }

    public boolean updateIssuance(Issuer issuer) {
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
                    return false;

                Book book = fetchBookByIsbn(issuance.getIsbn());
                int issuedCopies = book.getIssuedCopies();

                book.setIssuedCopies(issuedCopies + adjQty);
                ResponseEntity<IssuanceResponse> bookResponse = updateBook(book);
                log.info("Response of Book Update; {}", bookResponse);

                issuance.setNoOfCopies(issuer.getNoOfCopies());
                Issuer issuerUpdate = issuerRepository.save(issuance);
                log.info("Response of Book Update; {}", issuerUpdate);
                return true;
            } catch (Exception ex) {
                return false;
            }
        }
        return false;
    }


    private HttpHeaders createHeaders(final String uname, final String pword) {
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

