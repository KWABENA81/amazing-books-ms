package com.edu.issuermicroservice.service;


import com.edu.issuermicroservice.model.Issuer;
import com.edu.issuermicroservice.repo.IssuerRepository;
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
    public Issuer findByIsbn(String isbn) {
        List<Issuer> issueList = issuerRepository.findByIsbn(isbn);
        Optional<Issuer> optionalIssuer = issueList.stream().findFirst();
        return (optionalIssuer.isPresent())
                ? issueList.stream().findFirst().get()
                : optionalIssuer.orElse(null);
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

    private String issuanceHandlingStatus() {
        return new Random().nextBoolean() ? "SUCCESS" : "FAILURE";
    }

    public Issuer doIssuance(Issuer issuer) {
        //book
        String customerInfo = "555523177";
        StringBuilder sbuilder = new StringBuilder(customerInfo);
        sbuilder.append(issuanceHandlingStatus());
        sbuilder.append(UUID.randomUUID().toString());
        // issuer.setBookId(issuer.getBookId());
        issuer.setCustId(sbuilder.toString());
        issuer.setIsbn("DEFAULT");
        issuer.setNoOfCopies(2);
        return issuerRepository.save(issuer);
    }

}
