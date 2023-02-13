package com.edu.issuermicroservice.controller;

import com.edu.issuermicroservice.common.Book;
import com.edu.issuermicroservice.common.IssuanceRequest;
import com.edu.issuermicroservice.common.IssuanceResponse;
import com.edu.issuermicroservice.exceptions.IssuerNotFoundExceptionResponseStatus;
import com.edu.issuermicroservice.model.Issuer;
import com.edu.issuermicroservice.service.IssuerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@CrossOrigin(origins = {"${app.security.cors.origin}"})
@Api(value = "Issuer Class", protocols = "http")
@RequestMapping("/issuances")
public class IssuerController {
    @Autowired
    private IssuerService issuerService;

    @ApiOperation(value = "Fetch all Issuances", response = Issuer.class)
    @GetMapping
    public ResponseEntity<List<Issuer>> issuances() {
        log.info("Start All Issuances retrieval");
        List<Issuer> issuances = issuerService.findAll().stream().collect(Collectors.toList());
        return ResponseEntity.ok(issuances);
    }

    @ApiOperation(value = "Fetch Issue by Id", response = Issuer.class)
    @GetMapping(path = "/id/{id}")
    public ResponseEntity<Issuer> issuerById(@PathVariable(value = "id") Integer id) {
        Optional<Issuer> optionalIssuances = issuerService.findById(id);
        if (optionalIssuances.isPresent()) {
            log.info(" Issuer findById OK");
            return ResponseEntity.ok().body(optionalIssuances.get());
        } else {
            log.error("FindById Issuer failed");
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation(value = "Delete / Cancel Book Issue", response = Issuer.class)
    @PutMapping(path = "/updateIssuance/{id}")
    public void updateIssuance(@RequestBody Issuer issuer) {
        log.info("Issuer with #id {} has cancelIssue", issuer);
        boolean isRemoved = issuerService.updateIssuance(issuer);
        if (!isRemoved) {
            log.error("Issuer not found with id {}", issuer);
            throw new IssuerNotFoundExceptionResponseStatus(HttpStatus.NOT_FOUND);
        } else
            log.info("Issuer with #id {} has been deleted", issuer);
    }


    @ApiOperation(value = "Fetch Issuances by ISBN", response = Issuer.class)
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<List<Issuer>> findIssuancesByIsbn(@PathVariable String isbn) {
        List<Issuer> issuances = issuerService.findIssuancesByIsbn(isbn);
        log.info(" Issuer findIssuancesByIsbn OK {}", isbn);
        return ResponseEntity.ok(issuances);
    }

    @ApiOperation(value = "Fetch Book by ISBN", response = Book.class)
    @GetMapping("/fetchBook/{isbn}")
    public ResponseEntity<Book> fetchBookByIsbn(@PathVariable String isbn) {
        Book book = issuerService.fetchBookByIsbn(isbn);
        if (book != null) {
            log.info(" Issuer fetchBookByIsbn OK {}", book);
            return ResponseEntity.ok().body(book);
        } else {
            log.error("fetchBookByIsbn Issuer failed");
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation(value = "Issue Book to Customer", response = IssuanceResponse.class)
    @PostMapping(path = "/doIssuance")
    public IssuanceResponse doIssuance(@RequestBody IssuanceRequest request) {
        log.info(" Issuer doIssuance OK {}", request.toString());
        return issuerService.doIssuance(request);
    }
}