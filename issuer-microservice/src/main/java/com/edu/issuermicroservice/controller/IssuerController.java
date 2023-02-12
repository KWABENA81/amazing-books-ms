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

    @ApiOperation(value = "Delete / Cancel Book Issue", response = Issuer.class)
    @DeleteMapping(path = "/cancel/{id}")
    public void cancelIssuance(@PathVariable(value = "id") Integer id) {
        log.info("Issuer with #id {} has cancelIssue", id);
        boolean hasRemovedIssuance = issuerService.delete(id);
        if (!hasRemovedIssuance) {
            log.error("Issuer not found with id {}", id);
            throw new IssuerNotFoundExceptionResponseStatus(HttpStatus.NOT_FOUND);
        } else
            log.info("Issuer with #id {} has been deleted", id);
    }

    @ApiOperation(value = "Fetch Issuances by ISBN", response = Issuer.class)
    @GetMapping("/issuances/{isbn}")
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
// @ApiOperation(value = "Issue Book to customer", response = Issuer.class)
// @PostMapping(path = "/bookIssuance")
// public IssuanceResponse doIssuance(@RequestBody IssuanceRequest  issuanceRequest){
// return issuerService.saveIssuance(issuanceRequest);
// }

// @ApiOperation(value = "Issue Book to Customer", response = Book.class)
// @PostMapping(path = "/issueBook")
// public ResponseEntity<Book> issueBook(@RequestBody Book book) {
// // Book book=bookService. savedIssuer = issuerService.doIssuance(issuer);
// return null;//new ResponseEntity<>(savedIssuer, HttpStatus.OK);
// }


// @ApiOperation(value = "To create a book", response = Book.class, code = 200)
// @PostMapping("/create")
// public TransactionResponse create(@RequestBody TransactionRequest request) {
// return bookService.saveBook(request);
// }
// ===

// @ApiOperation(value = "Fetch Issue by Id", response = Issuer.class)
// @GetMapping(path = "/issuers/{id}")
//// @PrometheusTimeMethod(name =
// "issuer_resource_controller_issuer_by_id_duration_seconds", help = "Some
// helpful info here")
// public ResponseEntity<IssuanceResponse> issuerBooksById(@PathVariable(value =
// "id") Integer id) {
// IssuanceResponse issuanceResponse = new IssuanceResponse();
//
// log.info("Issuer issuerBooksById OK");
// Optional<Issuer> optionalIssuer = issuerService.findById(id);
// if (optionalIssuer.isPresent()) {
// Issuer isuance = optionalIssuer.get();
// issuanceResponse.setIssuer(isuance);
// issuanceResponse.setCustomerInfo(isuance.getCustId());
// log.info("74 Book issuerBooksById OK {}", isuance);
// List<Book> issuerBooks = null;
// try {
// log.info("74-77 Book issuerBooksById OK {}", issuerBooks);
// issuerBooks = restTemplate.getForObject(URLEncoder
// .encode("http://localhost:8097/book/issuer/" + id, "UTF-8"), List.class);
// // .encode("http://BOOK-MICROSERVICE/book/issuer/" + id, "UTF-8"),
// List.class);
// issuanceResponse.setBooks(issuerBooks);
// log.info("82 Book issuerBooksById OK {}", issuerBooks);
// return new ResponseEntity<>(issuanceResponse, HttpStatus.OK);
// } catch (UnsupportedEncodingException e) {
// log.error("85 UnsupportedEncodingException {}", e.getMessage());
// throw new RuntimeException(e);
// }
// } else {
// log.error("FindById Issuer failed");
// return ResponseEntity.notFound().build();
// }
// }
// ===
