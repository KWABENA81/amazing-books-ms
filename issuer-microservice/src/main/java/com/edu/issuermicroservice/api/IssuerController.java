package com.edu.issuermicroservice.api;


import com.edu.issuermicroservice.common.Book;
import com.edu.issuermicroservice.common.IssuerResponse;
import com.edu.issuermicroservice.model.Issuer;
import com.edu.issuermicroservice.service.IssuerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@CrossOrigin(origins = {"${app.security.cors.origin}"})
@Api(value = "Issuer Class", protocols = "http")
@RequestMapping("/issuer")
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
    @GetMapping(path = "/issuers")
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
    public ResponseEntity<IssuerResponse> issuerBooksById(@PathVariable(value = "id") Long id) {
        IssuerResponse issuerResponse = new IssuerResponse();

        log.info("Issuer issuerBooksById OK");
        Optional<Issuer> optionalIssuer = issuerService.findById(id);
        if (optionalIssuer.isPresent()) {
            Issuer isuance = optionalIssuer.get();
            issuerResponse.setIssuer(isuance);
            issuerResponse.setCustomerInfo(isuance.getCustomerInfo());
            log.info("74 Books issuerBooksById OK {}", isuance);
            List<Book> issuerBooks = null;
            try {
                log.info("74-77 Books issuerBooksById OK {}", issuerBooks);
                issuerBooks = restTemplate.getForObject(URLEncoder
                        .encode("http://localhost:8097/book/issuer/" + id, "UTF-8"), List.class);
                // .encode("http://BOOKMS/book/issuer/" + id, "UTF-8"), List.class);
                issuerResponse.setBooks(issuerBooks);
                log.info("82 Books issuerBooksById OK {}", issuerBooks);
                return new ResponseEntity<>(issuerResponse, HttpStatus.OK);
            } catch (UnsupportedEncodingException e) {
                log.error("85 UnsupportedEncodingException {}", e.getMessage());
                throw new RuntimeException(e);
            }

        } else {
            log.error("FindById Issuer failed");
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation(value = "Issue  to Issuer Customer", response = Issuer.class)
    @PostMapping(path = "/doIssue")
    public ResponseEntity<Issuer> doIssue(@RequestBody Issuer issuer) {
        Issuer savedIssuer = issuerService.doIssue(issuer);
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else
            log.info("Issuer with #id {} has been deleted", id);
    }
}


