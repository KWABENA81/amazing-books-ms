package com.edu.bookmicroservice.repo;


import com.edu.bookmicroservice.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    @Query("SELECT bk FROM Book bk WHERE bk.isbn=(:isbn)")
    Optional<Book> findByIsbn(@Param("isbn") String isbn);

//    @Query("SELECT bk FROM Book bk WHERE bk.issuanceId=(:id)")
//    List<Book> findByIssuanceId(@Param("id") Long id);
}

