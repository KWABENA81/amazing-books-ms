package com.edu.issuermicroservice.service;

import com.edu.issuermicroservice.model.Issuer;

import java.util.Collection;
import java.util.Optional;

public interface IIssuerService {
    Collection<Issuer> findAll();

    Optional<Issuer> findById(Integer id);

//    Issuer findBookByIsbn(String isbn);

    boolean delete(Integer id);

}
