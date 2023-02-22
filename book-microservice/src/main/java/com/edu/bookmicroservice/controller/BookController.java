package com.edu.bookmicroservice.controller;


import com.edu.bookmicroservice.entity.Book;
import com.edu.bookmicroservice.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = {"${app.security.cors.origin}"})
@Api(value = "Books MicroService", protocols = "http")
@RequestMapping(path = "/book")
public class BookController {

    private static final Logger logger = LogManager.getLogger(BookController.class);
    @Autowired
    private BookService bookService;


    @ApiOperation(value = "Fetch all books", response = Book.class, code = 200)
    @GetMapping
    public ResponseEntity<List<Book>> books() {
        logger.info("Start All Books retrieval");
        List<Book> books = bookService.findAll().stream().collect(Collectors.toList());
        return ResponseEntity.ok(books);
    }

    @ApiOperation(value = "Fetch book by ISBN", response = Book.class, code = 200)
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<Book> findByIsbn(@PathVariable(value = "isbn") String isbn) {
        logger.info("book is started findByIsbn {}", isbn);
        Optional<Book> bookOptional = bookService.findByIsbn(isbn);
        if (bookOptional.isPresent()) {
            logger.info(" Books findByIsbn OK");
            return ResponseEntity.ok().body(bookOptional.get());
        } else {
            logger.error("Start findByIsbn Books failed");
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation(value = "Fetch book by Id", response = Book.class, code = 200)
    @GetMapping("/id/{id}")
    public ResponseEntity<Book> booksById(@PathVariable(value = "id") Integer id) {
        Optional<Book> bookOptional = bookService.findById(id);
        if (bookOptional.isPresent()) {
            logger.info(" Books findById OK");
            return ResponseEntity.ok().body(bookOptional.get());
        } else {
            logger.error("Start findById Books failed");
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation(value = "To add a book", response = Book.class, code = 200)
    @PostMapping("/addBook")
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        logger.error("Adding Book {}", book.getIsbn());
        Book bk = bookService.addBook(book);
        if (bk == null) {
            logger.error("addBook Books failed");
            return ResponseEntity.notFound().build();
        } else {
            logger.info(" Books addBook OK");
            return ResponseEntity.ok().body(bk);
        }
    }

    @ApiOperation(value = "Edit existing book", response = Book.class, code = 200)
    @PutMapping("/editBook")
    public ResponseEntity<Book> editBook(@RequestBody Book book) {
        logger.error("Edit Book {}", book.getIsbn());
        Book bk =  bookService.updateBook(book);
        if (bk == null) {
            logger.error("editBook Books failed");
            return ResponseEntity.notFound().build();
        } else {
            logger.info(" Books editBook OK");
            return ResponseEntity.ok().body(bk);
        }
    }

    @ApiOperation(value = "Delete book", response = Book.class, code = 200)
    @DeleteMapping("/deleteBook")
    public boolean deleteBook(@RequestBody Book book) {
        logger.error("Delete Book {}", book.getIsbn());
        return bookService.deleteBook(book);
    }
}
