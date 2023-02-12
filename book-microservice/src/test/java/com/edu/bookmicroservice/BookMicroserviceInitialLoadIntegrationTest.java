package com.edu.bookmicroservice;

import com.edu.bookmicroservice.repo.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookMicroserviceInitialLoadIntegrationTest {

    @Autowired
    private
    BookRepository bookRepository;

//    @Test //    @Sql({"/schema.sql"})
//    public void testLoadDataForTestClass() {
//        assertEquals(5, bookRepository.findAll().size());
//    }
}
