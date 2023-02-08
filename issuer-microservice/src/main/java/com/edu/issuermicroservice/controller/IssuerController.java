package com.edu.issuermicroservice.controller;


import com.edu.issuermicroservice.common.Book;
import com.edu.issuermicroservice.common.IssuanceResponse;
import com.edu.issuermicroservice.exceptions.IssuerNotFoundExceptionResponseStatus;
import com.edu.issuermicroservice.model.Issuer;
import com.edu.issuermicroservice.service.IssuerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@CrossOrigin(origins = {"${app.security.cors.origin}"})
@Api(value = "Issuer Class", protocols = "http")
@RequestMapping("/issuers")
public class IssuerController {
    @Autowired
    private IssuerService issuerService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/intro")
    public String issuerIntro() {
        return "This is the Issuer microservice resource";
    }

    @ApiOperation(value = "Fetch all Issuers", response = Issuer.class)
    @GetMapping //(path = "/issuers")
    public ResponseEntity<List<Issuer>> issuers() {
        log.info("Start All Issuers retrieval");
        List<Issuer> issuers = issuerService.findAll().stream().collect(Collectors.toList());
        return ResponseEntity.ok(issuers);
    }

    @ApiOperation(value = "Fetch Issue by Id", response = Issuer.class)
    @GetMapping(path = "/id/{id}")
    //@PrometheusTimeMethod(name = "issuer_resource_controller_issuer_by_id_duration_seconds", help = "Some helpful info here")
    public ResponseEntity<Issuer> issuerById(@PathVariable(value = "id") Long id) {
        Optional<Issuer> issuerOptional = issuerService.findById(id);
        if (issuerOptional.isPresent()) {
            log.info(" Issuer findById OK");
            return ResponseEntity.ok().body(issuerOptional.get());
        } else {
            log.error("FindById Issuer failed");
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation(value = "Fetch Issue by Id", response = Issuer.class)
    @GetMapping(path = "/issuers/{id}")
//@PrometheusTimeMethod(name = "issuer_resource_controller_issuer_by_id_duration_seconds", help = "Some helpful info here")
    public ResponseEntity<IssuanceResponse> issuerBooksById(@PathVariable(value = "id") Long id) {
        IssuanceResponse issuanceResponse = new IssuanceResponse();

        log.info("Issuer issuerBooksById OK");
        Optional<Issuer> optionalIssuer = issuerService.findById(id);
        if (optionalIssuer.isPresent()) {
            Issuer isuance = optionalIssuer.get();
            issuanceResponse.setIssuer(isuance);
            issuanceResponse.setCustomerInfo(isuance.getCustId());
            log.info("74 Books issuerBooksById OK {}", isuance);
            List<Book> issuerBooks = null;
            try {
                log.info("74-77 Books issuerBooksById OK {}", issuerBooks);
                issuerBooks = restTemplate.getForObject(URLEncoder
                        .encode("http://localhost:8097/book/issuer/" + id, "UTF-8"), List.class);
                // .encode("http://BOOK-MICROSERVICE/book/issuer/" + id, "UTF-8"), List.class);
                issuanceResponse.setBooks(issuerBooks);
                log.info("82 Books issuerBooksById OK {}", issuerBooks);
                return new ResponseEntity<>(issuanceResponse, HttpStatus.OK);
            } catch (UnsupportedEncodingException e) {
                log.error("85 UnsupportedEncodingException {}", e.getMessage());
                throw new RuntimeException(e);
            }
        } else {
            log.error("FindById Issuer failed");
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation(value = "Issue Book to Customer", response = Issuer.class)
    @PostMapping(path = "/doIssuance")
    public ResponseEntity<Issuer> doIssuance(@RequestBody Issuer issuer) {
        Issuer savedIssuer = issuerService.doIssuance(issuer);
        return new ResponseEntity<>(savedIssuer, HttpStatus.OK);
    }


    @ApiOperation(value = "Delete / Cancel Book Issue", response = Issuer.class)
    @DeleteMapping(path = "/cancel/{id}")
    //@RequestMapping(value ="/cancel/{id}" , method = RequestMethod.DELETE)
    public void cancelIssue(@PathVariable(value = "id") Long id) {
        log.info("Issuer with #id {} has cancelIssue", id);
        boolean isRemoved = issuerService.delete(id);
        if (!isRemoved) {
            log.error("Issuer not found with id {}", id);
            throw new IssuerNotFoundExceptionResponseStatus(HttpStatus.NOT_FOUND);
        } else
            log.info("Issuer with #id {} has been deleted", id);
    }

    @GetMapping("/{isbn}")
    public Issuer findIssuerByIsbn(@PathVariable String isbn) throws JsonProcessingException {
        return issuerService.findIssuancesByIsbn(isbn);
    }

}


