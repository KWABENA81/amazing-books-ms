package com.edu.bookmicroservice.api;


import com.edu.bookmicroservice.common.TransactionRequest;
import com.edu.bookmicroservice.common.TransactionResponse;
import com.edu.bookmicroservice.model.Book;
import com.edu.bookmicroservice.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@CrossOrigin(origins = {"${app.security.cors.origin}"})
@Api(value = "Books Class", protocols = "http")
@RequestMapping(path = "/book")
public class BookController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BookService bookService;

    @GetMapping("/intro")
    public String bookIntro() {
        return "This is the Book microservice resource";
    }

    @ApiOperation(value = "Fetch all books", response = Book.class, code = 200)
    //@PrometheusTimeMethod(name = "ms_book_path_duration_seconds", help = "book microservice help")
    @GetMapping(path = "/books")
    public ResponseEntity<List<Book>> books() {
        log.info("Start All Books retrieval");
        List<Book> books = bookService.findAll().stream().collect(Collectors.toList());
        return ResponseEntity.ok(books);
    }

    @ApiOperation(value = "Fetch book by Id", response = Book.class, code = 200)
    @GetMapping("/id/{id}")
    public ResponseEntity<Book> booksById(@PathVariable(value = "id") Long id) {
        Optional<Book> bookOptional = bookService.findById(id);
        if (bookOptional.isPresent()) {
            log.info(" Books findById OK");
            return ResponseEntity.ok().body(bookOptional.get());
        } else {
            log.error("Start findById Books failed");
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation(value = "Fetch books by issuer Id", response = Book.class)
    @GetMapping("/issuer/{id}")
    public ResponseEntity<List<Book>> fetchByIssuerId(@PathVariable(value = "id") Long id) {
        log.info("67 Books fetchByIssuerId OK");
        List<Book> books = bookService.findByIssuerId(id);
        log.info("69 Books fetchByIssuerId OK {}", books);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }


    @ApiOperation(value = "To create a book", response = Book.class, code = 200)
    @PostMapping("/create")
    public TransactionResponse create(@RequestBody TransactionRequest request) {
        return bookService.saveBook(request);
    }

    @ApiOperation(value = "Update existing book", response = Book.class, code = 200)
    @PutMapping("/edit/{id}")
    public Book editBook(@RequestBody Book nbook, @PathVariable(value = "id") Long id) {
        return bookService.findById(id).map(bk -> {
            bk.setIsbn(nbook.getIsbn());
            bk.setTitle(nbook.getTitle());
            bk.setAuthor(nbook.getAuthor());
            bk.setTotalCopies(nbook.getTotalCopies());
            bk.setIssuerId(nbook.getIssuerId());
            bk.setPublishedDate(nbook.getPublishedDate());

            return bookService.save(bk);
        }).orElseGet(() -> {
            nbook.setId(id);
            bookService.save(nbook);
            return bookService.save(nbook);
        });
    }

    @ApiOperation(value = "Delete a book", response = Book.class, code = 200)
    @DeleteMapping(value = "/delete/{id}")
    public void deleteBook(@PathVariable(value = "id") Long id) {
        try {
            log.info("START:  Book with id {} Deleted", id);
            bookService.delete(id);
            log.info("FINISHED:  Book with id {} Deleted", id);
        } catch (Exception ex) {
            log.error("FAILED:  Book with id {} Deleted", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                log.error("ERROR PAGE -- error-404");
                return "error-404";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                log.error("ERROR PAGE -- error-500");
                return "error-500";
            }
        }
        return "error";
    }
}
