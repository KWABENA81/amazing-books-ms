package com.edu.bookmicroservice.controller;


import com.edu.bookmicroservice.entity.Book;
import com.edu.bookmicroservice.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@CrossOrigin(origins = {"${app.security.cors.origin}"})
@Api(value = "Books MicroService", protocols = "http")
@RequestMapping(path = "/books")
public class BookController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BookService bookService;


    @ApiOperation(value = "Fetch all books", response = Book.class, code = 200)
    @GetMapping
    public ResponseEntity<List<Book>> books() {
        log.info("Start All Books retrieval");
        List<Book> books = bookService.findAll().stream().collect(Collectors.toList());
        return ResponseEntity.ok(books);
    }

    @ApiOperation(value = "Fetch book by ISBN", response = Book.class, code = 200)
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<Book> findByIsbn(@PathVariable(value = "isbn") String isbn) {
        log.info("book is started findByIsbn {}", isbn);
        Optional<Book> bookOptional = bookService.findByIsbn(isbn);
        if (bookOptional.isPresent()) {
            log.info(" Books findByIsbn OK");
            return ResponseEntity.ok().body(bookOptional.get());
        } else {
            log.error("Start findByIsbn Books failed");
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation(value = "Fetch book by Id", response = Book.class, code = 200)
    @GetMapping("/id/{id}")
    public ResponseEntity<Book> booksById(@PathVariable(value = "id") Integer id) {
        Optional<Book> bookOptional = bookService.findById(id);
        if (bookOptional.isPresent()) {
            log.info(" Books findById OK");
            return ResponseEntity.ok().body(bookOptional.get());
        } else {
            log.error("Start findById Books failed");
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation(value = "To add a book", response = Book.class, code = 200)
    @PostMapping("/addBook")
    public Book addBook(@RequestBody Book book) {
        log.error("Adding Book {}", book.getIsbn());
        return bookService.addBook(book);
    }

    @ApiOperation(value = "Edit existing book", response = Book.class, code = 200)
    @PutMapping("/editBook")
    public Book editBook(@RequestBody Book book) {
        log.error("Edit Book {}", book.getIsbn());
        return bookService.updateBook(book);
    }

    @ApiOperation(value = "Delete book", response = Book.class, code = 200)
    @DeleteMapping("/deleteBook")
    public boolean deleteBook(@RequestBody Book book) {
        log.error("Delete Book {}", book.getIsbn());
        return bookService.deleteBook(book);
    }

}
