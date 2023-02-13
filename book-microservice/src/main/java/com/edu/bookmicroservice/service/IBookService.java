package com.edu.bookmicroservice.service;

import com.edu.bookmicroservice.model.Book;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface IBookService {

    Collection<Book> findAll();

   // List<Book> findByIssuanceId(Long Id);

    Optional<Book> findById(Long id);

    Optional<Book> findByIsbn(String isbn);

    void delete(Long id);
}
