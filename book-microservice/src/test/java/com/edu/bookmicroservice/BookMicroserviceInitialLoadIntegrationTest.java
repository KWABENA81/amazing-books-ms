package com.edu.bookmicroservice;

import com.edu.bookmicroservice.repo.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

//@RunWith(SpringRunner.class)
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
