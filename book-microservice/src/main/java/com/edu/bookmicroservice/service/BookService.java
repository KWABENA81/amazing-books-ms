package com.edu.bookmicroservice.service;



import com.edu.bookmicroservice.entity.Book;
import com.edu.bookmicroservice.repo.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Optional;


@Slf4j
@Service
public class BookService implements IBookService {

    @Autowired
    private BookRepository bookRepository;


    @Override
    public Collection<Book> findAll() {
        return bookRepository.findAll();
    }


    @Override
    public Optional<Book> findById(Integer id) {
        return bookRepository.findById(id);
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    @Override
    public void delete(Integer id) {
        log.info(" BookService:  Book with id {} Deleted", id);
        bookRepository.deleteById(id);
    }

    public Book save(Book book) {
        bookRepository.save(book);
        return book;
    }

//    public Book saveBook(Book book) {
//        bookRepository.save(book);
//        return book;
//    }


//    public TransactionResponse saveBook(TransactionRequest request) {
//        Book book = request.getBook();
//        Issuer issuer = request.getIssuer();
//        issuer.setIsbn(book.getIsbn());
//        issuer.setCopies(book.getTotalCopies());
//
//        Issuer issuerResponse = template
//                .postForObject("http://ISSUER-MICROSERVICE/issuer/doIssuance", issuer, Issuer.class);
//        String message = (issuerResponse.getStatus().equalsIgnoreCase("SUCCESS"))
//                ? "Issuer SUCCESSFULLY" : "Invalid entry, FAILURE";
//
//        bookRepository.save(book);
//        return new TransactionResponse(book, issuerResponse.getCustInfo(),
//                issuerResponse.getStatus(), issuerResponse.getTransactionId(), message);
//    }

}
