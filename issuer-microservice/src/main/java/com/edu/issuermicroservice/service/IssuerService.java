package com.edu.issuermicroservice.service;


import com.edu.issuermicroservice.model.Issuer;
import com.edu.issuermicroservice.repo.IssuerRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
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
        return issueList.stream().findFirst().get();
    }

    @Override
    public boolean delete(Long id) {
        try {
            issuerRepository.deleteById(id);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public String issueHandling() {
        return new Random().nextBoolean() ? "SUCCESS" : "FAILURE";
    }
//    public List<Issuer> findIssuerByCustomer(String custid) {
//        return null;//issuerRepository.findByCustId(custid);
//    }

//    public String customerBookIssuing() {
//        return new Random().nextBoolean() ? "SUCCESS" : "FAILURE";
//    }

    public Issuer doIssue(Issuer issuer) {
        issuer.setIssueStatus(issueHandling());
        // issuer.setBookId(issuer.getBookId());
        issuer.setIssueTransactionId(UUID.randomUUID().toString().replace("-", "").toUpperCase());
        issuer.setCustomerInfo("4164587787");
        issuer.setIsbn("DEFAULT");
        issuer.setNoOfCopies(102);
        return issuerRepository.save(issuer);
    }

//    public Issuer findIssuerByBookIsbn(String bookIsbn) {
//        List<Issuer> list = issuerRepository.findIssuerByBookIsbn(bookIsbn);
//        return (!list.isEmpty()) ? list.stream().findFirst().get() :
//                new Issuer(999, "nul", "nul", 0, "nul", "nul");
//    }


}
