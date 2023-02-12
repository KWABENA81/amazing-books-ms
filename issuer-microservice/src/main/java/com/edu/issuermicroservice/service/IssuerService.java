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
        Book book = fetchBookByIsbn(request.getBookIsbn());
        IssuanceResponse issuanceResponse = null;
        if (book != null && canIssueBook(book, request)) {
            Issuer issuer = new Issuer();
            String custInfo = customerInfo(request);
            issuer.setCustId(custInfo);
            issuer.setNoOfCopies(request.getRequestQty());

            //  save issuer
            issuerRepository.save(issuer);
            //  update book record
            Integer oldQty = book.getIssuedCopies();
            book.setIssuedCopies(oldQty + request.getRequestQty());
            ResponseEntity<IssuanceResponse> issuanceResponseRE = updateBook(book);

            log.info("ResponseEntity<IssuanceResponse> of book Update {}", issuanceResponseRE);
            issuanceResponse = new IssuanceResponse();
            issuanceResponse.setIssuer(issuer);
            issuanceResponse.setBook(book);
            issuanceResponse.setCustomerInfo(custInfo);
            issuanceResponse.setIssuanceStatus("SUCCESS");
        }
        return issuanceResponse;
    }

    private String customerInfo(IssuanceRequest request) {
        StringBuilder sbuilder = new StringBuilder();

        sbuilder.append(request.getCustomer().info()).append(FS)
                .append(UUID.randomUUID()).append(FS)
                .append(issuanceProcessing());
        return sbuilder.toString().toUpperCase();
    }

    private boolean canIssueBook(Book book, IssuanceRequest request) {
        return book != null && request != null
                && ((book.getTotalCopies() - book.getIssuedCopies()) > request.getRequestQty());
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
        String bookResourceUrl = bookResourceUpdateUrl + book.getId();
        ResponseEntity<IssuanceResponse> responseEntity = restTemplate.exchange(bookResourceUrl, HttpMethod.PUT,
                new HttpEntity<IssuanceResponse>(createHeaders("user", "Password")), IssuanceResponse.class);
        return responseEntity;
    }

    public boolean updateIssuance(Issuer issuer) {
        List<Issuer> issuances = findIssuancesByIsbn(issuer.getIsbn());
        //  get issuer with isbn & containing customer Id
        Optional<Issuer> issuanceOptional = issuances.stream()
                .filter(is -> is.getCustId().contains(issuer.getCustId())).findFirst();

        if (issuanceOptional.isPresent()) {
            Issuer issuance = issuanceOptional.get();
            issuance.setIsbn(issuer.getIsbn());
            issuance.setCustId(issuer.getCustId());
            issuance.setNoOfCopies(issuer.getNoOfCopies());

            Issuer updatedObj = issuerRepository.save(issuance);
            return (updatedObj != null /*&& updatedObj == issuer*/);
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

//    private Object requestCallback(final Book updatedBook) {
//        return request -> {
//            ObjectMapper mapper = new ObjectMapper();
//            mapper.writeValue(request.getBody(), updatedBook);
//            request.getHeaders().add(
//                    HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JASON_VALUE);
//            request.getHeaders().add(HttpHeaders.AUTHORIZATION, "Basic" + getBase64EncodedLogPass());
//        };
//    }

// @ApiOperation(value = "Issue Book to customer", response = Issuer.class)
// @PostMapping(path = "/bookIssuance")
// public IssuanceResponse doIssuance(@RequestBody IssuanceRequest  issuanceRequest){
// return issuerService.saveIssuance(issuanceRequest);
// }
//    public Issuer doIssuance(Issuer issuer, String isbn) {
//        StringBuilder sbuilder = new StringBuilder(customerInfo);
//        sbuilder.append(issuanceProcessing());
//        sbuilder.append(UUID.randomUUID().toString());
//
//        issuer.setCustId(sbuilder.toString().replace("-", "").toUpperCase(););
//        issuer.setBookId(issuer.getBookId());
//
//
//        issuer.setIsbn("DEFAULT");
//        issuer.setNoOfCopies(2);
//        return issuerRepository.save(issuer);
//    }
//    public IssuanceResponse saveIssuance(IssuanceRequest issuanceRequest) {
//        Customer customer = issuanceRequest.getCustomer();
//        String isbn = issuanceRequest.getBookIsbn();
//        Integer copies = issuanceRequest.getNoOfCopies();
//
//        template.postForObject("http://BOOK-MICROSERVICE/books/doIssuance")
//    }