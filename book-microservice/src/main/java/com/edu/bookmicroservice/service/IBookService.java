package com.edu.bookmicroservice.service;


import com.edu.bookmicroservice.entity.Book;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface IBookService {

    Collection<Book> findAll();

   // List<Book> findByIssuanceId(Integer Id);

    Optional<Book> findById(Integer id);

    Optional<Book> findByIsbn(String isbn);

    void delete(Integer id);
}
