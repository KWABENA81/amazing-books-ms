package com.edu.bookmicroservice;

import com.edu.bookmicroservice.entity.Book;
import com.edu.bookmicroservice.repo.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookMicroserviceDataSourceTests {

    @Autowired
    private BookRepository bookRepository;
//
//    @Test
//    @Transactional("bookTransactionManager")
//    public void create_check_book() {
//        Book book = new Book(112, "7yds5ts9",
//                "In The Animal Kingdom", LocalDate.now(),
//                12, 1, "JohnSmith");
//        book = bookRepository.save(book);
//        assertNotNull(bookRepository.findById(book.getId()));
//    }
}
