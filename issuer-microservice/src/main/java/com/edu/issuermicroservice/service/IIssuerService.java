package com.edu.issuermicroservice.service;

import com.edu.issuermicroservice.model.Issuer;

import java.util.Collection;
import java.util.Optional;

public interface IIssuerService {
    Collection<Issuer> findAll();

    Optional<Issuer> findById(Long id);

    Issuer findByIsbn(String isbn);

    boolean delete(Long id);

}
