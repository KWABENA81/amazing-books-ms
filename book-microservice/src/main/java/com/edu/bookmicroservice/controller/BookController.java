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

    @ApiOperation(value = "Fetch book by ISBN", response = Book.class, code = 200)
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<Book> booksByIsbn(@PathVariable(value = "isbn") String isbn) {
        Optional<Book> bookOptional = bookService.findByIsbn(isbn);
        if (bookOptional.isPresent()) {
            log.info(" Books findByIsbn OK");
            return ResponseEntity.ok().body(bookOptional.get());
        } else {
            log.error("Start findByIsbn Books failed");
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation(value = "Fetch all books", response = Book.class, code = 200)
    @GetMapping
    public ResponseEntity<List<Book>> books() {
        log.info("Start All Books retrieval");
        List<Book> books = bookService.findAll().stream().collect(Collectors.toList());
        return ResponseEntity.ok(books);
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
    @PostMapping("/add")
    public Book add(@RequestBody Book book) {
        log.error("Start adding Books{}", book);
        return bookService.save(book);
    }

    @ApiOperation(value = "Update existing book", response = Book.class, code = 200)
    @PutMapping("/edit/{id}")
    public Book editBook(@RequestBody Book nbook, @PathVariable(value = "id") Integer id) {
        return bookService.findById(id).map(bk -> {
            bk.setIsbn(nbook.getIsbn());
            bk.setTitle(nbook.getTitle());
            bk.setAuthor(nbook.getAuthor());
            bk.setTotalCopies(nbook.getTotalCopies());
            // bk.setIssuanceId(nbook.getIssuanceId());
            bk.setPublishedDate(nbook.getPublishedDate());

            return bookService.save(bk);
        }).orElseGet(() -> {
            nbook.setId(id);
            bookService.save(nbook);
            return bookService.save(nbook);
        });
    }
}
//    @RequestMapping("/error")
//    public String handleError(HttpServletRequest request) {
//        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
//
//        if (status != null) {
//            Integer statusCode = Integer.valueOf(status.toString());
//
//            if (statusCode == HttpStatus.NOT_FOUND.value()) {
//                log.error("ERROR PAGE -- error-404");
//                return "error-404";
//            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
//                log.error("ERROR PAGE -- error-500");
//                return "error-500";
//            }
//        }
//        return "error";
//    }


//    @ApiOperation(value = "Delete a book", response = Book.class, code = 200)
//    @DeleteMapping(value = "/delete/{id}")
//    public void deleteBook(@PathVariable(value = "id") Integer id) {
//        try {
//            log.info("START:  Book with id {} Deleted", id);
//            bookService.delete(id);
//            log.info("FINISHED:  Book with id {} Deleted", id);
//        } catch (Exception ex) {
//            log.error("FAILED:  Book with id {} Deleted", id);
//            throw new BookNotFoundExceptionResponseStatus(HttpStatus.NOT_FOUND);
//        }
//    }