package com.edu.bookmicroservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.persistence.*;
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

    @NonNull
    private String isbn;

    @NonNull
    private String title;

    @NonNull
    private LocalDate publishedDate = LocalDate.now();

    @NonNull
    private Integer totalCopies;

    @NonNull
    private Integer issuedCopies;

    @NonNull
    private String author;


}
