package com.edu.issuermicroservice.controller;

import com.edu.issuermicroservice.common.Book;
import com.edu.issuermicroservice.common.IssuanceRequest;
import com.edu.issuermicroservice.common.IssuanceResponse;
import com.edu.issuermicroservice.exceptions.IssuerNotFoundExceptionResponseStatus;
import com.edu.issuermicroservice.model.Issuer;
import com.edu.issuermicroservice.service.IssuerService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@CrossOrigin(origins = {"${app.security.cors.origin}"})
@Api(value = "Issuer Class", protocols = "http")
@RequestMapping("/issuers")
public class IssuerController {
    private static final Logger logger= LogManager.getLogger(IssuerController.class);
    @Autowired
    private IssuerService issuerService;
    @Autowired
    @Lazy
    private RestTemplate restTemplate;

    @ApiOperation(value = "Fetch all Issuances", response = Issuer.class)
    @GetMapping(path = "/issuances")
    public ResponseEntity<List<Issuer>> issuances() {
        logger.info("Start All Issuances retrieval");
        List<Issuer> issuances = issuerService.findAll().stream().collect(Collectors.toList());
        return ResponseEntity.ok(issuances);
    }

    @ApiOperation(value = "Fetch Issue by Id", response = Issuer.class)
    @GetMapping(path = "/id/{id}")
    public ResponseEntity<Issuer> issuerById(@PathVariable(value = "id") Integer id) {
        Optional<Issuer> optionalIssuances = issuerService.findById(id);
        if (optionalIssuances.isPresent()) {
            logger.info(" Issuer findById OK");
            return ResponseEntity.ok().body(optionalIssuances.get());
        } else {
            logger.error("FindById Issuer failed");
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation(value = "Update  Issuance", response = Issuer.class)
    @PutMapping(path = "/updateIssuance/{id}")
    public void updateIssuance(@RequestBody Issuer issuer) {
        logger.info("Issuer with #id {} has cancelIssue", issuer);
        boolean isRemoved = issuerService.updateIssuance(issuer);
        if (!isRemoved) {
            logger.error("Issuer not found with id {}", issuer);
            throw new IssuerNotFoundExceptionResponseStatus(HttpStatus.NOT_FOUND);
        } else
            logger.info("Issuer with #id {} has been deleted", issuer);
    }


    @ApiOperation(value = "Fetch Issuances by ISBN", response = Issuer.class)
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<List<Issuer>> findIssuancesByIsbn(@PathVariable(value = "isbn") String isbn) {
        List<Issuer> issuances = issuerService.findIssuancesByIsbn(isbn);
        logger.info(" Issuer findIssuancesByIsbn OK {}", isbn);
        return ResponseEntity.ok(issuances);
    }

    @ApiOperation(value = "Fetch Book by ISBN", response = Book.class)
    @GetMapping("/fetchBook/{isbn}")
    @HystrixCommand(fallbackMethod = "fetchBookFallback"
    /*,
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000"),
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10")
            }*/
    )
    public ResponseEntity<Book> fetchBookByIsbn(@PathVariable (value = "isbn")String isbn) {
        Book book = restTemplate.getForObject(issuerService.bookResourceIsbnUrl + isbn, Book.class);
        // issuerService.fetchBookByIsbn(isbn);
        if (book != null) {
            logger.info(" Issuer fetchBookByIsbn OK {}", book);
            return ResponseEntity.ok().body(book);
        } else {
            logger.error("fetchBookByIsbn Issuer failed");
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Book> fetchBookFallback(@PathVariable(value = "isbn") String isbn) {
        return ResponseEntity.ok().body(new Book());
    }

    @ApiOperation(value = "Issue Book to Customer", response = IssuanceResponse.class)
    @PostMapping(path = "/doIssuance")
    public IssuanceResponse doIssuance(@RequestBody IssuanceRequest request) {
        logger.info(" Issuer doIssuance OK {}", request.toString());
        return issuerService.doIssuance(request);
    }
}