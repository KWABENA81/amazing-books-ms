package com.edu.issuermicroservice.repo;


import com.edu.issuermicroservice.entity.Issuer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssuerRepository extends JpaRepository<Issuer, Integer> {

    @Query("SELECT iss FROM Issuer iss WHERE iss.isbn=(:isbn)")
    List<Issuer> findIssuanceByBookIsbn(@Param("isbn") String isbn);
}