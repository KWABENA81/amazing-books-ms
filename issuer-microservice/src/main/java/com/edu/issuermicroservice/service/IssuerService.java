package com.edu.issuermicroservice.service;


import com.edu.issuermicroservice.model.Issuer;
import com.edu.issuermicroservice.repo.IssuerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class IssuerService implements IIssuerService {

    @Autowired
    private IssuerRepository issuerRepository;

    public Collection<Issuer> findAll() {
        return issuerRepository.findAll();
    }

    @Override
    public Optional<Issuer> findById(Long id) {
        return issuerRepository.findById(id);
    }



    @Override
    public boolean delete(Long id) {
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

    public Issuer doIssuance(Issuer issuer) {
        //book
        String customerInfo = "555523177";
        StringBuilder sbuilder = new StringBuilder(customerInfo);
        sbuilder.append(issuanceProcessing());
        sbuilder.append(UUID.randomUUID().toString());
        // issuer.setBookId(issuer.getBookId());
        issuer.setCustId(sbuilder.toString());
        issuer.setIsbn("DEFAULT");
        issuer.setNoOfCopies(2);
        return issuerRepository.save(issuer);
    }

    public Issuer findIssuancesByIsbn(String isbn) throws JsonProcessingException {
        List<Issuer> issuances = issuerRepository.findIssuanceByBookIsbn(isbn);
        Issuer issuer = issuances.stream().findFirst().get();
        log.info("Issuer findIssuancesByIsbn : {}", new ObjectMapper().writeValueAsString(issuer));
        return issuer;
    }

    //@Override
    public Issuer findBooksByIsbn(String isbn) {
        List<Issuer> issueList = issuerRepository.findIssuanceByBookIsbn(isbn);
        Optional<Issuer> optionalIssuer = issueList.stream().findFirst();
        return (optionalIssuer.isPresent())
                ? issueList.stream().findFirst().get()
                : optionalIssuer.orElse(null);
    }
}
