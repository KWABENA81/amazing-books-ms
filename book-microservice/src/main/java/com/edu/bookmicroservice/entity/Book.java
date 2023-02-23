package com.edu.bookmicroservice.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Entity;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BOOK_TB")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String isbn = "Ako087DEFAULT";

    private String title = "Book Title DEFAULT";

    private LocalDate publishedDate = LocalDate.now();

    private Integer totalCopies = 111;

    private Integer issuedCopies = 1;

    private String author = "Main Author";

}
